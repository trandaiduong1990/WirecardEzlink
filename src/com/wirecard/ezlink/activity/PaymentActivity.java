package com.wirecard.ezlink.activity;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.R;
import com.wirecard.ezlink.constants.StringConstants;
import com.wirecard.ezlink.fragment.ContactFragment;
import com.wirecard.ezlink.fragment.HelpFragment;
import com.wirecard.ezlink.fragment.PaymentFragment;
import com.wirecard.ezlink.fragment.TagCardFragment;
import com.wirecard.ezlink.fragment.TermsAndConditionsFragment;
import com.wirecard.ezlink.fragment.TransactionHistoryFragment;
import com.wirecard.ezlink.handle.Util;
import com.wirecard.ezlink.handle.ConnectionDetector;
import com.wirecard.ezlink.handle.IsoDepReaderTask;
import com.wirecard.ezlink.handle.ReaderModeAccess;
import com.wirecard.ezlink.handle.WebserviceConnection;
import com.wirecard.ezlink.model.Card;
import com.wirecard.ezlink.model.ErrorCode;
import com.wirecard.ezlink.model.QRCode;
import com.wirecard.ezlink.navigationdrawer.DrawerItemCustomAdapter;
import com.wirecard.ezlink.navigationdrawer.ObjectDrawerItem;
import com.wirecard.ezlink.webservices.receipt.RecieptReqError;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.NfcAdapter.ReaderCallback;
import android.nfc.TagLostException;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PaymentActivity extends FragmentActivity {
	private NfcAdapter mNfcAdapter;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private Button payButton;
	private WebserviceConnection wsConnection;
	private QRCode qrCode;
	private ConnectionDetector cd;
	private Card card;
	private String cardNo;
	private boolean detectCard;
	private String debitCommand;
	private ErrorCode errorCode;
	private String paymentAmt;
	private String merchantName;
	private String purseBalance;
	private ProgressDialog dialog;
	private PendingIntent pendingIntent;
	private IntentFilter[] filters;
	private String[][] techList;
	private ReaderModeAccess readerModeAccess;
	
	private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private boolean tapCardAgain;
    private FragmentManager fragmentManager;
    private boolean noReceiptResponse;
    private boolean excuseDebit;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e("PAYMENT ACTIVITY", "first line On Create..");
		super.onCreate(savedInstanceState);
		Log.e("PAYMENT ACTIVITY", "2 On Create..");
		setContentView(R.layout.activity_payment);
		Log.e("PAYMENT ACTIVITY", "On Create..");
		
		fragmentManager = getSupportFragmentManager();
		// for proper titles
		mTitle = mDrawerTitle = getTitle();

		// initialize properties
		mNavigationDrawerItemTitles = getResources().getStringArray(
				R.array.navigation_drawer_payment);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// list the drawer items
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];

		drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_action_copy,
				"Payment");
		drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_action_share,
				"Transaction History");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_action_share,
				"Contact");
		drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_action_share,
				"Terms & Conditions");
		drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_action_share, "Help");

		// Pass the folderData to our ListView adapter
		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this,
				R.layout.listview_item_row, drawerItem);

		// Set the adapter for the list view
		mDrawerList.setAdapter(adapter);

		// set the item click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// for app icon control for nav drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			selectItem(0);
		}

		sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		card = new Card();
		errorCode = new ErrorCode(this);
		cd = new ConnectionDetector(this);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		wsConnection = new WebserviceConnection(this);

		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		filters = new IntentFilter[] { new IntentFilter(
				NfcAdapter.ACTION_TECH_DISCOVERED) };
		techList = new String[][] { new String[] { IsoDep.class.getName() } };
		handleIntent(getIntent());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
       if (mDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }
       
       return super.onOptionsItemSelected(item);
	}
	
	// to change up caret
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	
	
	// navigation drawer click listener
	private class DrawerItemClickListener implements OnItemClickListener {
		
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        selectItem(position);
	    }
	    
	}

    private void selectItem(int position) {
    	
        // update the main content by replacing fragments
    	
        Fragment fragment = null;
        
        switch (position) {
        case 0:
            fragment = new PaymentFragment();
            break;
        case 1:
            fragment = new TagCardFragment();
            break;
        case 2:
        	fragment = new ContactFragment();
        	break;
        case 3:
        	fragment = new TermsAndConditionsFragment();
        	break;
        case 4:
        	fragment = new HelpFragment();
        	break;
        default:
            break;
        }
        
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "fragment"+position).commit();
//            fragmentManager.beginTransaction().add(R.id.content_frame, fragment, "fragment"+position).commit();
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
	@Override
    protected void onResume() {
        super.onResume();
        cd.ensureSensorIsOn();
		mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList);
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		PaymentFragment paymentFragment = (PaymentFragment) fragmentManager.findFragmentByTag("fragment0");
		if(paymentFragment != null) {
			Log.d("PaymentFragment is", " not null");
//			cardNo_textView = (TextView) paymentFragment.getView().findViewById(R.id.cardNo_textView1);
//			purseBalance_textView = (TextView) paymentFragment.getView().findViewById(R.id.purseBalance_textView);
//			paymentAmt_textView = (TextView) paymentFragment.getView().findViewById(R.id.paymentAmt_textView1);
//			merchantName_textView = (TextView) paymentFragment.getView().findViewById(R.id.merchantName_textView1);
//			merchantRef_textView = (TextView) paymentFragment.getView().findViewById(R.id.merchantRef_textView);
			payButton = (Button) paymentFragment.getView().findViewById(R.id.payButton);
//			guide_textView = (TextView) paymentFragment.getView().findViewById(R.id.guide_textView);
			
			showInfo();
		} else {
			Log.d("PaymentFragment is", " null");
		}
//		isoDepReaderTask.countDownTimeConnectToCard();
    }
	
	private void showInfo() {
		qrCode = QRCode.getInstance();
		paymentAmt = qrCode.getQR_AMOUNT();
		merchantName = qrCode.getQR_MER_NAME();
//		String merchantRef = qrCode.getQR_MER_TRAX_REF_NO();
		cardNo = sharedPreferences.getString("cardNo", null);
		purseBalance = sharedPreferences.getString("balance", null);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mNfcAdapter.disableForegroundDispatch(this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		if(Util.getVibratePref(this)) {
			((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100L);
		}
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			detectCard = true;
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			final IsoDep isoDep = IsoDep.get(tag);
			TagCardFragment tagCardFragment = (TagCardFragment) fragmentManager.findFragmentByTag("fragment1");
			if(tagCardFragment != null) {
				dialog = ProgressDialog.show(PaymentActivity.this, "Please hold on to your card", "Scanning...", true);
				new com.wirecard.ezlink.handle.IsoDepReaderTask(this, null, null, dialog, true).execute(isoDep);
			} else {
				payButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog = ProgressDialog.show(PaymentActivity.this, "Processing", "Please wait while your transaction is being processed...", true);
						// Request : Debit Command
						if(tapCardAgain) {
							new CurrentBalanceTask().execute(isoDep);
						} else {
							new IsoDepReaderTask().execute(isoDep);
						}
					}
				});
			}
		}
	}
	
	public class IsoDepReaderTask extends AsyncTask<IsoDep, Void, String> {

		public IsoDepReaderTask() {
		}

		@Override
		protected String doInBackground(IsoDep... params) {
			Log.e("PAYMENT ACTIVITY", "ISODEPREADER TASK DO in Back ground..");
			String errorStr = null;
			try{
			IsoDep isoDep = params[0];
			
			if (isoDep != null) {
				try {
					Log.d("isConnected", isoDep.isConnected()+"");
					if (!isoDep.isConnected()) {
						isoDep.connect();
						Log.d("isConnected2", isoDep.isConnected()+"");
					}
					isoDep.setTimeout(5000);

					byte[] initByte = new byte[8];
					initByte[1] = -92;
					initByte[4] = 2;
					initByte[5] = 64;
		            byte[] initRespose = isoDep.transceive(initByte);
		            Log.d("init()", "request: " + Util.hexString(initByte));
			        Log.d("init()", "response: " + Util.hexString(initRespose));
			        
					byte[] getChallengeByte = { (byte) 0x00, (byte) 0x84,
							(byte) 0x00, (byte) 0x00, (byte) 0x08 };
					byte[] challengeResult = isoDep.transceive(getChallengeByte);
					Log.d("challengeResult2",Util.hexString(challengeResult));
					String cardRN = Util.hexString(challengeResult).substring(0, 16);
					
					String purseRequest = sharedPreferences.getString("purseRequest", null);
					byte[] purseByte = Util.hexStringToByteArray(purseRequest.toString());
					byte[] purseResult = isoDep.transceive(purseByte);
					String purseData = Util.hexString(purseResult);
					Log.d("purseData2", purseData);
					if(purseData.length() < 48) {
//						errorCode.sendErrorToReceipt(qrCode, ErrorCode.getErrorCode24() + ":" + ErrorCode.getInvalidCommandFromCard());
						errorStr = ErrorCode.getInvalidCommandFromCard();
						return errorStr;
					}
					String cardNumber = card.getCardNo(purseData);
					if(!cardNo.equals(cardNumber)) {
						wsConnection.uploadReceiptData(qrCode, "", new RecieptReqError(ErrorCode.getErrorCode15(), ErrorCode.getInvalidCard()));
						errorStr = getResources().getString(R.string.error_invalid_card);
						return errorStr;
					}
					
					if(!excuseDebit) {
						// Execute "Debit Command"
						editor.putString("cardRN", cardRN);
						editor.putString("purseData", purseData);
						editor.commit();
						Log.d("debitCommand", "++CALL WS++"+System.currentTimeMillis());
						debitCommand = wsConnection.getDebitCommand(qrCode);
						Log.d("debitCommand", "++RECIEVED WS++"+System.currentTimeMillis());
						
						//check if errorCode is null or not. If it is 12, so payment is timeout
						String errorCode = sharedPreferences.getString("errorCode", null);
						if(errorCode != null && errorCode.equals("12")) {
							errorStr = ErrorCode.getTimeOut();
							editor.putString("errorCode", null);
							editor.commit();
							return errorStr;
						}
					}
					if (debitCommand != null) {
						if(!excuseDebit) {
							noReceiptResponse = true;
							debitCommand = "90340000" + debitCommand + "00";
							excuseDebit = true;
						} else {
//							isoDep.transceive(initByte);
//							isoDep.transceive(getChallengeByte);
						}
						Log.d("debitCommand", debitCommand);
//						SystemClock.sleep(1000);
						byte[] debitCommandByte = Util.hexStringToByteArray(debitCommand);
						byte[] receiptByte = isoDep.transceive(debitCommandByte);
						// the mobile get receipt data from card
						noReceiptResponse = false;
						
						String receiptData = Util.hexString(receiptByte);
						Log.d("receiptData", receiptData);
						
//						check receiptData
						if(!receiptData.contains("9000")) {
							Log.d("Receipt Data error", "!9000");
							//check purse balance is updated or not
							isoDep.transceive(getChallengeByte);
							byte[] purseResult2 = isoDep.transceive(purseByte);
							String purseData2 = Util.hexString(purseResult2);
							if(purseData2.length() < 48) {
//								errorCode.sendErrorToReceipt(qrCode, ErrorCode.getErrorCode24() + ":" + ErrorCode.getInvalidCommandFromCard());
								errorStr = ErrorCode.getInvalidCommandFromCard();
								return errorStr;
							}
							Double curentBal2 = Double.parseDouble(card.getPurseBal(purseData2));
							// debit command is successful 
							if(curentBal2 < Double.parseDouble(purseBalance)) {
								wsConnection.uploadReceiptData(qrCode, "", new RecieptReqError(ErrorCode.getErrorCode01(), ErrorCode.getDebitCommandSuccessfulButNoResponseFromCard()));
								Bundle b = new Bundle();
								b.putString("cardNo", cardNo);
								b.putString("merchantName", merchantName);
								b.putString("paymentAmt", paymentAmt);
								b.putString("prevBal", purseBalance);
								b.putString("currentBal", String.valueOf(curentBal2));
								Intent in = new Intent(PaymentActivity.this, ConfirmationActivity.class);
								in.putExtras(b);
								startActivity(in);
								finish();
								return null;
							} else {
								//wsConnection.uploadReceiptData(qrCode, "", new (ErrorCode.getErrorCode25(), ErrorCode.getApplicationError()));
								errorStr = StringConstants.ErrorRemarks.TRANX_FAILURE;
								return errorStr;
							}
						}
						
						Log.d("RECIEPT", "++CALL WS++"+System.currentTimeMillis());
						// Upload receipt data
						wsConnection.uploadReceiptData(qrCode, receiptData, new RecieptReqError(ErrorCode.getErrorCode00(), ErrorCode.getSuccessful()));
						Log.d("RECIEPT", "++RESULT WS++"+System.currentTimeMillis());
						
						new CurrentBalanceTask().execute(isoDep);
					}
					else{
						errorStr=ErrorCode.getConnectionIssue();
						return errorStr;
					}
					
				}catch (Exception e) {
					errorStr=ErrorCode.getTagLost();
					Log.e("doInBackgroundError", e.toString());
				} finally {
					//errorStr="TAG LOST";
					try {
						isoDep.close();
					}catch (IOException e) {
						errorStr=ErrorCode.getConnectionCloseIssue();
//						errorCode.sendError(qrCode, e.getMessage());
						Log.e("doInBackgroundErrorfINALLY", e.getMessage());
					}
					
				}
			}
			}
			catch(Exception e){
				errorStr=ErrorCode.getConnectionIssue();
			}
			return errorStr;
		}


		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null != result) {
				PaymentFragment.error_textView.setVisibility(View.VISIBLE);
				PaymentFragment.error_content.setText(result);
			}
			if (dialog != null && dialog.isShowing())
	        {
				dialog.dismiss();
				dialog = null;
	        }
			if(PaymentFragment.dialog != null && PaymentFragment.dialog.isShowing()) {
				PaymentFragment.dialog.dismiss();
				PaymentFragment.dialog = null;
			}
		}
	}
	
	class CurrentBalanceTask extends AsyncTask<IsoDep, Void, String> {
		
		@Override
		protected String doInBackground(IsoDep... params) {
			String errorStr = null;
			try {
				IsoDep isoDep = params[0];
				Log.d("isConnected in CurrentBalanceTask", isoDep.isConnected()+"");
				if (!isoDep.isConnected()) {
					isoDep.connect();
				}
				isoDep.setTimeout(5000);
				if (tapCardAgain || noReceiptResponse) {
					byte[] initByte = new byte[8];
					initByte[1] = -92;
					initByte[4] = 2;
					initByte[5] = 64;
					isoDep.transceive(initByte);
				}
				tapCardAgain = true;
				byte[] getChallengeByte = { (byte) 0x00, (byte) 0x84,
						(byte) 0x00, (byte) 0x00, (byte) 0x08 };
				isoDep.transceive(getChallengeByte);
				
				String purseRequest = sharedPreferences.getString("purseRequest", null);
				byte[] purseByte = Util.hexStringToByteArray(purseRequest.toString());
				byte[] purseResult = isoDep.transceive(purseByte);
				String purseData = Util.hexString(purseResult);
				Log.d("purseData3", purseData);
				if(purseData.length() < 10) {
	//				errorCode.sendErrorToReceipt(qrCode, ErrorCode.getErrorCode24() + ":" + ErrorCode.getInvalidCommandFromCard());
					errorStr = ErrorCode.getInvalidCommandFromCard();
					return errorStr;
				}
				
				String cardNumber = card.getCardNo(purseData);
				if(!cardNo.equals(cardNumber)) {
					wsConnection.uploadReceiptData(qrCode, "", new RecieptReqError(ErrorCode.getErrorCode15(), ErrorCode.getInvalidCard()));
					errorStr = getResources().getString(R.string.error_invalid_card);
					return errorStr;
				}
				
				String currentBalance = card.getPurseBal(purseData);
				Log.d("preBal", purseBalance);
				Log.d("curentBal", currentBalance + "");
				// check the card balance is correct after payment is successful by comparing with  old and new balances.
				boolean diffirentBalance = card.checkCurrentBalance(currentBalance, purseBalance, paymentAmt);
				if(!diffirentBalance) {
//					wsConnection.uploadReceiptData(qrCode, "", new RecieptReqError(ErrorCode.getErrorCode15(), ErrorCode.getInvalidCard()));
					errorStr = "Card balance is not correct";
					return errorStr;
//					final Toast toast = Toast.makeText(PaymentActivity.this, "Card balance is not correct", Toast.LENGTH_LONG);
//					toast.show();
//					new CountDownTimer(4000, 1000)
//					{
//					    public void onTick(long millisUntilFinished) {toast.show();}
//					    public void onFinish() {
//					    	toast.show();
//					    }
//					}.start();
				}
				
				editor.clear();
				editor.commit();
				
				//can not get receipt data from card
				/*if(noReceiptResponse) {
					wsConnection.uploadReceiptData(qrCode, "", new RecieptReqError(ErrorCode.getErrorCode01(), ErrorCode.getDebitCommandSuccessfulButNoResponseFromCard()));
				}*/
				Bundle b = new Bundle();
				b.putString("cardNo", cardNo);
				b.putString("merchantName", merchantName);
				b.putString("paymentAmt", paymentAmt);
				b.putString("prevBal", purseBalance);
				b.putString("currentBal", currentBalance);
				Intent in = new Intent(PaymentActivity.this, ConfirmationActivity.class);
				in.putExtras(b);
				startActivity(in);
				finish();
			}catch (Exception e) {
				errorStr=ErrorCode.getTagLost();
				Log.e("doInBackgroundError", e.toString());
			}
			return errorStr;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(null != result) {
				detectCard = false;
				PaymentFragment.error_textView.setVisibility(View.VISIBLE);
				PaymentFragment.error_content.setText(result);
			}
			if (dialog != null && dialog.isShowing())
	        {
				dialog.dismiss();
				dialog = null;
	        }
			if(PaymentFragment.dialog != null && PaymentFragment.dialog.isShowing()) {
				PaymentFragment.dialog.dismiss();
				PaymentFragment.dialog = null;
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onBackPressed(){
		//dialog.dismiss();
		/*
		Intent in = new Intent(PaymentActivity.this, NFCActivity.class);
		
		startActivity(in);
		finish();
		PaymentActivity.super.onBackPressed();
		 */
		
		//---------------------
		 new AlertDialog.Builder(this)
	        .setTitle("Scan again..!!")
	        .setMessage("Are you sure you want to scan QR code again..?")
	        .setNegativeButton(android.R.string.no, null)
	        .setPositiveButton(android.R.string.yes, new android.content.DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg) {
					Intent in = new Intent(PaymentActivity.this, SecondActivity.class);
					
					startActivity(in);
					finish();
					PaymentActivity.super.onBackPressed();
						
				}

				
	        }).create().show();
	       
	}
	
}
