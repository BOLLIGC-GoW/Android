package swiftproduct.swift;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Edit_Page extends AppCompatActivity {








   /* Bundle bundle;
    EditText CmpName,ProductName,PRice,UNit,ShortDesc;
    Button submitButton, returnButton;
    dbHelper myDBHelper;
    int ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__page);
        bundle = getIntent().getExtras();
        if(bundle != null) {
            ID = bundle.getInt("ID");
            String cmpName = bundle.getString("cmpName");
            String productName = bundle.getString("productName");
            double Price = bundle.getDouble("Price");
            double Unit = bundle.getDouble("Unit");
            String shortDesc = bundle.getString("shortDesc");

            myDBHelper = new dbHelper(this);
            CmpName = (EditText) findViewById(R.id.edit_Company_NameEdit);
            CmpName.setText(cmpName);
            ProductName = (EditText) findViewById(R.id.edit_Product_NameEdit);
            ProductName.setText(productName);
            PRice = (EditText) findViewById(R.id.edit_PriceEdit);
            PRice.setText (String.valueOf(Price));
            UNit = (EditText) findViewById(R.id.edit_UnitEdit);
            UNit.setText(String.valueOf(Unit));
            ShortDesc = (EditText) findViewById(R.id.edit_Short_DescEdit);
            ShortDesc.setText(shortDesc);

            submitButton = (Button) findViewById(R.id.Submit_Edit_Product_Button);
            returnButton = (Button) findViewById(R.id.ReturntoMain_EditPage);
            onClickReturnButton();
            addData();
        }

    }
    public void addData(){

        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDBHelper.updateData(
                                ID,
                                CmpName.getText().toString(),
                                ProductName.getText().toString(),
                                PRice.getText().toString(),
                                UNit.getText().toString(),
                                ShortDesc.getText().toString()
                        );
                        if (isInserted == true)
                            Toast.makeText(Edit_Page.this, "Product Accepted", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Edit_Page.this, "Product Not Accepted", Toast.LENGTH_LONG).show();

                        //Go back to Main_Menu
                        Intent intent = new Intent("swiftproduct.swift.Main_Menu");
                        startActivity(intent);
                        finish();
                    }

                }
        );
    }

    public void onClickReturnButton(){

        returnButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent ("swiftproduct.swift.Main_Menu");
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
*/
}
