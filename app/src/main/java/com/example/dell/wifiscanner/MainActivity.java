package com.example.dell.wifiscanner;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Delayed;

public class MainActivity extends AppCompatActivity {

    private Element []nets;
    private WifiManager wifiManager;
    private List<ScanResult> wifiList;
    Dialog dialog;
    List<String> list = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {

                detectWiFi();
                Snackbar.make(view, "Scanning....", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });
    }

    /*public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            mWifiListener.getScanningResults();
        }
    }*/

    public void detectWiFi(){
        //TODO: Permission!!!
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        }else{
            //getScanningResults();
            //do something, permission was previously granted; or legacy device

            this.wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            this.wifiManager.startScan();
            this.wifiList = this.wifiManager.getScanResults();
        }*/
        this.wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        this.wifiManager.startScan();
        this.wifiList = this.wifiManager.getScanResults();

        Log.d("TAG",wifiList.toString());

        this.nets = new Element[wifiList.size()];
        for(int i = 0; i<wifiList.size(); i++){
            String item = wifiList.get(i).toString();
            String[] vector_item = item.split(",");
            String item_ssid = vector_item[0];
            String item_capabilities = vector_item[2];
            String item_level = vector_item[3];
            String ssid = item_ssid.split(":")[1];
            String security = item_capabilities.split(":")[1];
            String level = item_level.split(":")[1];
            nets[i] = new Element(ssid, security,level);
        }

        final AdapterElements adapterElements = new AdapterElements(this);
        final ListView netlist = (ListView) findViewById(R.id.listItem);
        final TextView info = (TextView) findViewById(R.id.info);
        netlist.setAdapter(adapterElements);

      //  Toast.makeText(MainActivity.this, "This is my Toast message!", Toast.LENGTH_LONG).show();
        netlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

              //  Toast.makeText(MainActivity.this,"Send",Toast.LENGTH_SHORT).show();
                int c = 0,k;
                String name = nets[i].getTitle();
              //  list.add("abc");

                //   Toast.makeText(getApplicationContext(), "This is my Toast message!", Toast.LENGTH_SHORT).show();
                while (c<10) {
                    for(k =0; (k < wifiList.size()) && (name.compareTo(nets[k].getTitle()) != 0); k++);

                    list.add(nets[k].getLevel());
                 //   String name = nets[i].getTitle();

                    Log.d("round", "roundc"+ c + nets[k].getTitle()+ nets[k].getLevel());
                    c++;
                    SystemClock.sleep(2000);
                   // Toast.makeText(getApplicationContext(), "This is my Toast message!", Toast.LENGTH_SHORT).show();
                    detectWiFi();
                  //  Toast.makeText(MainActivity.this,"Send", Toast.LENGTH_SHORT).show();
                }
               // Toast.makeText(MainActivity.this,"Send", Toast.LENGTH_SHORT).show();

                for(int z =0; z<list.size(); z++){

                    Log.d("melist", list.get(z));
                }
                Intent move = new Intent(MainActivity.this, Main2Activity.class);
                move.putExtra("demo","demo");
                startActivity(move);
                list.clear();

            }
        });



    }

    public void move(View view){
        Intent move = new Intent(MainActivity.this, Main2Activity.class);

        move.putExtra("demo","demo");
        startActivity(move);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class AdapterElements extends ArrayAdapter<Object>{
        Activity context;

        public AdapterElements(Activity context){
            super(context, R.layout.items, nets);
            this.context = context;
        }
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = context.getLayoutInflater();
            View item = inflater.inflate(R.layout.items, null);

            TextView tvSsid = (TextView) item.findViewById(R.id.tvSSID);
            tvSsid.setText(nets[position].getTitle());

            TextView tvSecurity = (TextView) item.findViewById(R.id.tvSecurity);
            tvSecurity.setText(nets[position].getSecurity());

            TextView tvLevel = (TextView) item.findViewById(R.id.tvLevel);
            tvLevel.setText("Signal Level: " + nets[position].getLevel());
            //String level = nets[position].getLevel();

           /* try{
                int i = Integer .parseInt(level);
                if (i>-50){
                    tvLevel.setText("High");
                }else if(i<=-50 && i>=-80){
                    tvLevel.setText("Average");
                }else if (i<=-80){
                    tvLevel.setText("Low");
                }
            } catch(NumberFormatException e){
                Log.d("TAG","Incorrect Format Line");
            }*/
            return item;
         }
    }

    public void showdialow(final String ten_Wifi) {
        dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Enter Wifi Password");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);

        Button btn_DongY = (Button) dialog.findViewById(R.id.btn_dongy);
        Button btn_Huy = (Button) dialog.findViewById(R.id.btn_huy);
        final EditText edt_Password = (EditText) dialog.findViewById(R.id.edt_password);

        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.cb_show);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    edt_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else {
                    edt_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        btn_DongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matkhau = edt_Password.getText().toString();

                if (TextUtils.isEmpty(matkhau)) {
                    edt_Password.setError("No password Yet");
                }
                else{
                    //Toast.makeText(MainActivity.this, "Name of wifi: " + ten_wifi + " Password " + matkhau, Toast.LENGTH_SHORT).show();
                  //  dialog.dismiss();

                }
            }
        });
        btn_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void connectToWifi (final String networkSSID, final String networkPassword){
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", networkSSID);
        conf.preSharedKey = String.format("\"%s\"", networkPassword);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);

        //Toast.makeText(MainActivity.this, "Name of wifi: " + networkSSID + " Password " + networkPassword, Toast.LENGTH_SHORT).show();


        int netId = wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list )
        {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }

        /*wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();*/
    }
}
