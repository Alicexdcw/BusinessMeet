package tw.com.bussinessmeet.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import tw.com.bussinessmeet.bean.UserInformationBean;
import tw.com.bussinessmeet.helper.DBHelper;

public class UserInformationDAO {
    private String whereClause = "blue_tooth = ?";
    private String tableName = "User_Information";
    private  String[] column = new String[]{"blue_tooth", "user_name", "company", "position","email","tel","avatar"};
    private SQLiteDatabase db ;
    public UserInformationDAO(DBHelper DH){
        db = DH.getWritableDatabase();
    }
    private ContentValues putValues(UserInformationBean userInformationBean){
        ContentValues values = new ContentValues();
        values.put("blue_tooth", userInformationBean.getBlueTooth());
        values.put("user_name", userInformationBean.getUserName());
        values.put("company",userInformationBean.getCompany());
        values.put("position", userInformationBean.getPosition());
        values.put("email",userInformationBean.getEmail());
        values.put("tel",userInformationBean.getTel());
        values.put("avatar", userInformationBean.getAvatar());
        return  values;
    }
    public void add (UserInformationBean userInformationBean){
        Log.d("add:","add");

        Log.d("add","dbsuccess");
        ContentValues values = putValues(userInformationBean);

        db.insert(tableName, null, values);
        db.close();
    }

    public void update(UserInformationBean userInformationBean){

        ContentValues values = putValues(userInformationBean);
    //範例
//        String[] whereArgs1 = {"#100", b.getStorage_id()};
//        String whereClause1 = DatabaseSchema.TABLE_TALKS.COLUMN_TID + "=? AND " + DatabaseSchema.TABLE_TALKS.COLUMN_STORAGEID + "=?";
//        db.update(DatabaseSchema.TABLE_TALKS.NAME, values1, whereClause1, whereArgs1);
        db.update(tableName, values,whereClause , new String[]{userInformationBean.getBlueTooth()});
    }
    public  void delete(String blueTooth){

        db.delete(tableName, whereClause,new String[]{blueTooth});
        db.close();
    }
    public String getById(String blueTooth) {
        Cursor cursor = db.query(tableName, null, "blue_tooth = ?", new String[]{blueTooth}, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("blue_tooth");
        Log.d("resultIndex",String.valueOf(index));
        try{
            return cursor.getString(cursor.getColumnIndex("blue_tooth"));
        }catch (Exception e){
            return null;
        }

    }
    public Cursor searchAll(UserInformationBean userInformationBean){

        String blueTooth = userInformationBean.getBlueTooth();
        String userName = userInformationBean.getUserName();
        String company = userInformationBean.getCompany();
        String position = userInformationBean.getPosition();
        String where = " ";
        ArrayList<String> args = new ArrayList<>();

        if(blueTooth != null && !blueTooth.equals(" ")){
            where += "blue_tooth = ?";
            args.add(userInformationBean.getBlueTooth());
        }
        if(userName != null && !userName.equals((""))){
            if(!where.equals(" "))where += " and ";
            where += "user_name = ?";
            args.add(userInformationBean.getUserName());
        }
        if(company != null && !company.equals(" ")){
            if(!where.equals(" "))where += " and ";
            where += "company = ?";
            args.add( userInformationBean.getCompany());
        }
        if(position != null && !position.equals(" ")){
            if(!where.equals(" "))where += " and ";
            where += "position = ?";
            args.add(userInformationBean.getPosition());
        }
        Cursor cursor = db.query(tableName, column, where,args.toArray(new String[0]),null,null,null);

        return cursor;
    }

}
