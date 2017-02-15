package com.projectone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {

    EditText createProcess;
    TextView icpn, icp, ics, irpn, irp, irs, ibpn, ibp, ibs;
    String sicpn, sicp, sics, sirpn, sirp, sirs, sibpn, sibp, sibs, ticpn, ticp, tics;
    boolean itemInL=false;
    boolean itemInB=false;
    int currentTotal=0;
    RadioButton blockRadio, switchRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createProcess = (EditText) findViewById(R.id.editText);

        icpn = (TextView) findViewById(R.id.inputCurrentProcessNo);
        icp = (TextView) findViewById(R.id.inputCurrentPriority);
        ics = (TextView) findViewById(R.id.inputCurrentState);

        irpn = (TextView) findViewById(R.id.inputReadyProcessNo);
        irp = (TextView) findViewById(R.id.inputReadyPriority);
        irs = (TextView) findViewById(R.id.inputReadyState);

        ibpn = (TextView) findViewById(R.id.inputBlockProcessNo);
        ibp = (TextView) findViewById(R.id.inputBlockPriority);
        ibs = (TextView) findViewById(R.id.inputBlockState);

        icpn.setText("");
        icp.setText("");
        ics.setText("");

        irpn.setText("");
        irp.setText("");
        irs.setText("");

        ibpn.setText("");
        ibp.setText("");
        ibs.setText("");

        sicpn=sicp=sics=sirpn=sirp=sirs=sibpn=sibp=sibs=ticpn=ticp=tics=sicpn=ticpn=ticp=tics="";
        blockRadio = (RadioButton) findViewById(R.id.blockQueueitem);
        switchRadio = (RadioButton) findViewById(R.id.ContextSwitchitem);
    }

    public void PrePopulate(View view){
        createNewProcess(0, 9, true, true);
        createNewProcess(0, 4, true, true);
        createNewProcess(0, 2, false, true);
        createNewProcess(0, 1, false, true);
        createNewProcess(0, 5, true, true);
        createNewProcess(0, 9, true, true);
        preInvis();
        postEverything();
    }

    private void createNewProcess(int a,int b, boolean c, boolean d){
        int index;
        if(d){
            index=currentTotal;
            currentTotal=currentTotal+1;
        }else{
            index=a;
        }
        if (c) {
            if (!itemInL) {
                sirpn = Integer.toString(index);
                sirp = Integer.toString(b);
                sirs = String.valueOf(c);
                itemInL = true;
            } else {
                sirpn = sirpn + "\n" + Integer.toString(index);
                sirp = sirp + "\n" + Integer.toString(b);
                sirs = sirs + "\n" + String.valueOf(c);
            }
        }else{
            if(!itemInB){
                sibpn=Integer.toString(index);
                sibp=Integer.toString(b);
                sibs="false";
                itemInB=true;
            }else{
                sibpn=sibpn+"\n"+Integer.toString(index);
                sibp=sibp+"\n"+Integer.toString(b);
                sibs=sibs+"\nfalse";
            }
        }
    }


    private void postEverything(){
        sortQueue();
        icpn.setText(sicpn);
        icp.setText(sicp);
        ics.setText(sics);

        irpn.setText(sirpn);
        irp.setText(sirp);
        irs.setText(sirs);

        ibpn.setText(sibpn);
        ibp.setText(sibp);
        ibs.setText(sibs);
    }

    public void unblock(View view){
        String[] rpnT=parsingstring(sibpn);
        String[] rpT=parsingstring(sibp);
        String[] rsT=parsingstring(sibs);
        int len=rpnT.length;
        boolean flag=true;
        if(parsingstring(sirs)[0].equals("true")){
            sirpn=sirpn+"\n"+rpnT[0];
            sirp=sirp+"\n"+rpT[0];
            sirs=sirs+"\n"+"true";
        }else{
            sirpn=rpnT[0];
            sirp=rpT[0];
            sirs="true";
        }
        sibpn=sibp=sibs="";
        for(int i = 1;i<len;i=i+1){
            if(flag) {
                sibpn=rpnT[i];
                sibp=rpT[i];
                sibs=rsT[i];
                flag=false;
            }else{
                sibpn=sibpn+"\n"+rpnT[i];
                sibp=sibp+"\n"+rpT[i];
                sibs=sibs+"\n"+rsT[i];
            }
        }
        postEverything();
    }

    private void sortQueue(){
        String[] rpnT=parsingstring(sirpn);
        String[] rpT=parsingstring(sirp);
        String[] rsT=parsingstring(sirs);
        String tempPn, tempP, tempS;
        int len=rpnT.length;
        for(int i1=0;i1<len-1;i1=i1+1) {
            for (int i2 = i1; i2 < len; i2 = i2 + 1) {
                if (Integer.parseInt(rpT[i1]) > Integer.parseInt(rpT[i2])) {
                    tempPn = rpnT[i2];
                    tempP = rpT[i2];
                    tempS = rsT[i2];
                    rpnT[i2] = rpnT[i1];
                    rpT[i2] = rpT[i1];
                    rsT[i2] = rsT[i1];
                    rpnT[i1] = tempPn;
                    rpT[i1] = tempP;
                    rsT[i1] = tempS;
                }
            }
        }
        sirpn=unparsestring(rpnT);
        sirp=unparsestring(rpT);
        sirs=unparsestring(rsT);
    }

    private String unparsestring(String[] x){//transforms array back into a string
        String output="";
        boolean flag=true;
        int totallen=x.length;
        for(int i=0;i<totallen;i=i+1){
            if(flag) {
                output = x[i];
                flag=false;
            }else{
                output=output+"\n"+x[i];
            }
        }
        return output;
    }

    private String[] parsingstring(String x){//transforms string to array
        String delim="\n";
        return x.split(delim);
    }

    public void TimeSlice(View view){//advances a time slice
        if(switchRadio.isChecked()|| !blockRadio.isChecked()) {
            String[] sirpnT = parsingstring(sirpn);
            String[] sirpT = parsingstring(sirp);
            String[] sirsT = parsingstring(sirs);
            if (sirsT[0].equals("true")) {
                String tempSicpn = sicpn;
                String tempSicp = sicp;
                String tempSics = sics;
                sicpn = sirpnT[0];
                sicp = sirpT[0];
                sics = sirsT[0];
                sirs = sirp = sirpn = "";
                if(sirpnT.length<=1){
                    sirpn=tempSicpn;
                    sirp=tempSicp;
                    sirs=tempSics;
                }else {
                    boolean flag = true;
                    for (int i = 1; i < sirpnT.length; i = i + 1) {
                        if (flag) {
                            sirpn = sirpnT[i];
                            sirp = sirpT[i];
                            sirs = sirsT[i];
                            flag = false;
                        } else {
                            sirpn = sirpn + "\n" + sirpnT[i];
                            sirp = sirp + "\n" + sirpT[i];
                            sirs = sirs + "\n" + sirsT[i];
                        }
                    }
                    if (tempSics.equals("true")) {
                        sirpn = sirpn + "\n" + tempSicpn;
                        sirp = sirp + "\n" + tempSicp;
                        sirs = sirs + "\n" + tempSics;
                    }
                }
            }else{
                if(sics.equals("true")){
                    sirpn=sicpn;
                    sirp=sicp;
                    sirs=sics;
                    sics=sicp=sicpn="";
                }else {
                    Toast.makeText(this, "Must have an item in the queue", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            if(sics.equals("true")){
                String tempSicpn = sicpn;
                String tempSicp = sicp;
                sicpn="";
                sicp="";
                sics="";
                if(parsingstring(sibs)[0].equals("false")) {
                    sibpn = sibpn + "\n" + tempSicpn;
                    sibp = sibp + "\n" + tempSicp;
                    sibs = sibs + "\nfalse";
                }else{
                    sibpn = tempSicpn;
                    sibp = tempSicp;
                    sibs = "false";
                }
            }else{
                Toast.makeText(this, "Must have a running process", Toast.LENGTH_SHORT).show();
            }
        }
        try{postEverything();}catch(Exception e){Toast.makeText(this, "3 "+e.toString(), Toast.LENGTH_SHORT).show();}
    }

    public void Terminate(View view){
        deleteFromRunning();
    }

    private void deleteFromRunning(){
        sicpn=sicp=sics="";
        postEverything();
    }

    public void CreateProcess(View view){//adds new process to list
        try {
            createNewProcess(0, Integer.parseInt(createProcess.getText().toString()), true, true);
            postEverything();
            preInvis();
        }catch(Exception e){
            Toast.makeText(getBaseContext(), "You must enter a number", Toast.LENGTH_SHORT).show();
        }
    }

    private void preInvis(){//gets rid of prepop button
        Button setsav4O = (Button) findViewById(R.id.Preopulate);
        setsav4O.setVisibility(View.INVISIBLE);
    }

    public void BlockItem(View view){
        switchRadio.setChecked(false);
    }

    public void ContextItem(View view){
        blockRadio.setChecked(false);
    }


}
