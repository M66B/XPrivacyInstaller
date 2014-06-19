package biz.bokhorst.xprivacy.installer;

import biz.bokhorst.xprivacy.installer.R;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;

public class ActivityMain extends Activity {

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Uri inputUri = Uri.parse(intent.getDataString());
			if (inputUri.getScheme().equals("package")) {
				String packageName = inputUri.getSchemeSpecificPart();
				if ("de.robv.android.xposed.installer".equals(packageName)) {
					CheckBox cbXposed = (CheckBox) findViewById(R.id.cbXposedInstalled);
					cbXposed.setChecked(true);
				} else if ("biz.bokhorst.xprivacy".equals(packageName)) {
					CheckBox cbXPrivacy = (CheckBox) findViewById(R.id.cbXPrivacy);
					cbXPrivacy.setChecked(true);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Register for new package notifications
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addDataScheme("package");
		registerReceiver(mReceiver, intentFilter);

		// Reference controls
		CheckBox cbAndroid = (CheckBox) findViewById(R.id.cbAndroid);
		TextView tvAndroid = (TextView) findViewById(R.id.tvAndroid);
		Button btnWhatIs = (Button) findViewById(R.id.btnWhatIs);
		LinearLayout llRoot = (LinearLayout) findViewById(R.id.llRoot);
		final Button btnCheckRoot = (Button) findViewById(R.id.btnCheckRoot);
		final CheckBox cbRoot = (CheckBox) findViewById(R.id.cbRoot);
		final Button btnRoot = (Button) findViewById(R.id.btnRoot);
		final LinearLayout llBackup = (LinearLayout) findViewById(R.id.llBackup);
		CheckBox cbBackup = (CheckBox) findViewById(R.id.cbBackup);
		final LinearLayout llSettings = (LinearLayout) findViewById(R.id.llSettings);
		final CheckBox cbSettings = (CheckBox) findViewById(R.id.cbSettings);
		final Button btnSettings = (Button) findViewById(R.id.btnSettings);
		final LinearLayout llXposedInstalled = (LinearLayout) findViewById(R.id.llXposedInstalled);
		final CheckBox cbXposedInstalled = (CheckBox) findViewById(R.id.cbXposedInstalled);
		final Button btnXposedInstalled = (Button) findViewById(R.id.btnXposedInstalled);
		final LinearLayout llXPosedEnabled = (LinearLayout) findViewById(R.id.llXPosedEnabled);
		final CheckBox cbXposedEnabled = (CheckBox) findViewById(R.id.cbXposedEnabled);
		final Button btnXposedEnabled = (Button) findViewById(R.id.btnXposedEnabled);
		final LinearLayout llXPrivacy = (LinearLayout) findViewById(R.id.llXPrivacy);
		final CheckBox cbXPrivacy = (CheckBox) findViewById(R.id.cbXPrivacy);
		final Button btnXPrivacy = (Button) findViewById(R.id.btnXPrivacy);
		final LinearLayout llEnabled = (LinearLayout) findViewById(R.id.llEnabled);
		final CheckBox cbEnabled = (CheckBox) findViewById(R.id.cbEnabled);
		final Button btnEnabled = (Button) findViewById(R.id.btnEnabled);
		final LinearLayout llActivate = (LinearLayout) findViewById(R.id.llActivate);
		final Button btnStartOnce = (Button) findViewById(R.id.btnStartOnce);
		final Button btnReboot = (Button) findViewById(R.id.btnReboot);
		final Button btnHelp = (Button) findViewById(R.id.btnHelp);
		final Button btnRate = (Button) findViewById(R.id.btnRate);

		// What is? help
		View.OnClickListener xda = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http://forum.xda-developers.com/showthread.php?t=2320783"));
					startActivity(intent);
				} catch (Throwable ex) {
				}
			}
		};
		btnWhatIs.setOnClickListener(xda);
		btnHelp.setOnClickListener(xda);

		btnRate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					final Uri uri = Uri.parse("market://details?id=" + getPackageName());
					final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
					if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
						startActivity(rateAppIntent);
				} catch (Throwable ex) {
				}
			}
		});

		// Android version
		cbAndroid.setChecked(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
			tvAndroid.setVisibility(View.VISIBLE);
		else
			llRoot.setVisibility(View.VISIBLE);

		// Check root
		btnCheckRoot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				boolean root = false;
				try {
					root = RootTools.isAccessGiven();
				} catch (Throwable ignore) {
				}
				btnCheckRoot.setEnabled(!root);
				cbRoot.setChecked(root);
				cbRoot.setVisibility(View.VISIBLE);
				btnRoot.setVisibility(root ? View.GONE : View.VISIBLE);
				llBackup.setVisibility(root ? View.VISIBLE : View.GONE);
			}
		});

		// Root
		btnRoot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http://www.androidcentral.com/root"));
					startActivity(intent);
				} catch (Throwable ex) {
				}
			}
		});

		// Backup
		cbBackup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					btnSettings.setVisibility(cbSettings.isChecked() ? View.GONE : View.VISIBLE);
				llSettings.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		// Security settings
		cbSettings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnSettings.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				if (isChecked) {
					boolean installed = isInstalled("de.robv.android.xposed.installer");
					cbXposedInstalled.setChecked(installed);
					btnXposedInstalled.setVisibility(installed ? View.GONE : View.VISIBLE);
				}
				llXposedInstalled.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
					startActivity(intent);
				} catch (Throwable ex) {
				}
			}
		});

		// Xposed installed
		cbXposedInstalled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnXposedInstalled.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				if (isChecked)
					btnXposedEnabled.setVisibility(cbXposedEnabled.isChecked() ? View.GONE : View.VISIBLE);
				llXPosedEnabled.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnXposedInstalled.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http://dl.xposed.info/latest.apk"));
					startActivity(intent);
				} catch (Throwable ex) {
				}
			}
		});

		// Xposed enabled
		cbXposedEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnXposedEnabled.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				if (isChecked) {
					boolean installed = isInstalled("biz.bokhorst.xprivacy");
					cbXPrivacy.setChecked(installed);
					btnXPrivacy.setVisibility(installed ? View.GONE : View.VISIBLE);
				}
				llXPrivacy.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnXposedEnabled.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
					intent.setPackage("de.robv.android.xposed.installer");
					intent.putExtra("section", "install");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} catch (Throwable ex) {
				}
			}
		});

		// XPrivacy installed
		cbXPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnXPrivacy.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				if (isChecked)
					btnEnabled.setVisibility(cbEnabled.isChecked() ? View.GONE : View.VISIBLE);
				llEnabled.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnXPrivacy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http://repo.xposed.info/module/biz.bokhorst.xprivacy/download/stable"));
					startActivity(intent);
				} catch (Throwable ex) {
				}
			}
		});

		// XPrivacy enabled
		cbEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnEnabled.setVisibility(isChecked ? View.GONE : View.VISIBLE);
				llActivate.setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});

		btnEnabled.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
					intent.setPackage("de.robv.android.xposed.installer");
					intent.putExtra("section", "modules");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} catch (Throwable ex) {
				}
			}
		});

		btnStartOnce.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					Intent intent = getPackageManager().getLaunchIntentForPackage("biz.bokhorst.xprivacy");
					startActivity(intent);
				} catch (Throwable ex) {
				}
			}
		});

		btnReboot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					CommandCapture command = new CommandCapture(0, "reboot now");
					RootTools.getShell(true).add(command);
				} catch (Throwable ex) {
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	public boolean isInstalled(String packageName) {
		try {
			getPackageManager().getPackageInfo(packageName, 0);
			return true;
		} catch (NameNotFoundException ignored) {
			return false;
		} catch (Throwable ex) {
			return false;
		}
	}
}
