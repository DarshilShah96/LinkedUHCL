/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkeduhcl_project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author darsh
 */
public class accountcreator {
    //three important variables for database operations

    static Connection conn = null;
    static Statement st = null;
    static ResultSet rs = null;
    //first of all, we need the db location                     

    static String url = "jdbc:mysql://mis-sql.uhcl.edu/shahd9440?useSSL=false";
    static String db_id = "shahd9440";
    static String db_psw = "1771687";

    public static void signup() {

        String Id = "", Password, Type, Company, Name;
        Scanner input = new Scanner(System.in);

        //prompts and input
        do {
            System.out.println("Please enter your Id");
            Id = input.nextLine();
        } while (!isValidid(Id));

        do {
            System.out.println("Please enter your Password");
            Password = input.nextLine();
        } while (Password == Id);

        System.out.println("Please enter your  Type");
        Type = input.nextLine();

        System.out.println("Please enter your Company");
        Company = input.nextLine();

        System.out.println("Please enter your Name");
        Name = input.nextLine();

        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();
            //we can ask user to enter id, pwd, type, compay..
            //to save time , we just use hardcoding

            int rs = st.executeUpdate("insert into profile values('" + Id + "','" + Password + "','" + Type + "','" + Company + "','" + Name + "')");
            System.out.println("the signup is done!");

        } catch (SQLException e) {
            // to print out the exception message
            e.printStackTrace();
        } finally {
            // close the db connections
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static boolean isValidid(String accountid) {

        String regex = "^(?=.*[a-zA-Z])(?=.*[#!*?])(?=.*[0-9])(?=\\S+$).{3,10}$";
        Pattern p = Pattern.compile(regex);
        if (accountid == "" || accountid == null) {
            return false;
        }
        Matcher m = p.matcher(accountid);
        return m.matches();
    }

    public static String login() {
        String id, password;
        Scanner input = new Scanner(System.in);

        //prompts and input
        System.out.println("Please enter your Id");
        id = input.nextLine();

        System.out.println("Please enter your Password");
        password = input.nextLine();

        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();

            rs = st.executeQuery("SELECT * FROM profile where Id='" + id + "' and Password='" + password + "'");

//            System.out.println(rs.getString("Id")+rs.getString("Password"));
            if (rs.next()) {

                return (rs.getString("Id") + "," + rs.getString("Company") + "," + rs.getString("Type") + "," + rs.getString("Name"));
            } else {
                return "Login failed !!";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Internal error";
        } finally {
            //close the database
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

    }

    public static ArrayList<String> displayrecommendations(String id, String company) {

        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();

            ArrayList<String> suggestions = new ArrayList<String>();

            //to get the account number
            rs = st.executeQuery("select * from profile where Id in (select Distinct Id from profile where  Id!='" + id + "' and Company='" + company + "'"
                    
                    + "and Id not in (select  Receiver_ID from connection where Sender_ID ='" + id + "'and status!='deny')"
                    + "and Id not in (select  Sender_ID from connection where Receiver_ID ='" + id + "'and status!='deny')"
                    + ")limit 5");

            while (rs.next()) {
                suggestions.add(rs.getString("Name") + " is from " + rs.getString("Company") + " ," + rs.getString("Id"));
            }

            rs = st.executeQuery(
                    "select * from profile where Id in "
                    + " ("
                    + " (select distinct Receiver_ID from connection where Sender_ID in"
                    + " (("
                    + " select Receiver_ID from connection where Receiver_ID in (select Receiver_ID from connection where Sender_ID='" + id + "' and status='approve')"
                    + " union "
                    + " select Sender_ID  from connection where Sender_ID in (select Sender_ID from connection where Receiver_ID='" + id + "' and status='approve')"
                    + " )))"
                    + " union "
                    + " ("
                    + " select  distinct Sender_ID from connection where Receiver_ID in"
                    + " (("
                    + " select Receiver_ID from connection where Receiver_ID in (select Receiver_ID from connection where Sender_ID='" + id + "' and status='approve')"
                    + " union "
                    + " select Sender_ID  from connection where Sender_ID in (select Sender_ID from connection where Receiver_ID='" + id + "' and status='approve')"
                    + " )))"
                    + " )"
                    + "and Id not in (select Receiver_ID from connection where Sender_ID='" + id + "' and status='approve')"
                    + "and Id not in (select Sender_ID from connection where Receiver_ID='" + id + "' and status='approve')"
                    + " and Id != '" + id + "' "
                    + "limit 5 ");

            while (rs.next()) {
                suggestions.add(rs.getString("Name") + " is from " + rs.getString("Company") + " ," + rs.getString("Id"));
            }

            return suggestions;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        } finally {
            //close the database
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

    }

    public static ArrayList<String> displayJobAds(String id) {

        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();

            ArrayList<String> jads = new ArrayList<String>();

            rs = st.executeQuery(
                    "select * from job_ad "
                    + "  where Job_ID in (select distinct Job_ID from job_share"
                    + "  where Profile_ID in ( select Receiver_ID from connection where Sender_ID='" + id + "' and status ='approve') "
                    + " or Profile_ID in ( select Sender_ID from connection where Receiver_ID='" + id + "' and status ='approve') "
                    + " ) "
                    + " and Job_ID not in (select Job_ID from job_share where Profile_ID = '" + id + "') "
                    + " order by Date desc "
                    + "limit 5 "
            );

            while (rs.next()) {
                jads.add("AWS " + rs.getString("Description") + " ," + rs.getString("Job_ID"));
            }

            return jads;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //close the database
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

    }

    public static ArrayList<String> seeMyConnections(String id, Boolean f) {
        // code to be executed
        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();

            ArrayList<String> acc = new ArrayList<String>();
            rs = st.executeQuery(" SELECT * FROM profile where id in "
                    + "("
                    + "(select Receiver_ID from connection where Sender_ID= '" + id + "' and status = 'approve') "
                    + "union "
                    + "(select Sender_ID from connection where Receiver_ID= '" + id + "' and status = 'approve') "
                    + ") "
                    + "and id != '" + id + "'"
            );

            if (f == true) {
                while (rs.next()) {
                    acc.add(rs.getString("Name") + " from " + rs.getString("Company") + " ," + rs.getString("Id"));
                }
                return acc;
            } else {
                System.out.println("The connections of the Person are :");
                while (rs.next()) {
                    System.out.println(rs.getString("Name") + " of " + rs.getString("Company") + " company.");
                }
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //close the database
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

    }

    static void sendConnectionRequest(String senderid, String receiverid) {
        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();

            int rs = st.executeUpdate("insert into connection values('" + senderid + "', '" + receiverid + "', " + "'new' )");
            if (rs == 1) {
                System.out.println("Connection request sent sucessfully !!");
            } else {
                System.out.println("Request sent Failed !!");
            }

        } catch (SQLException e) {
            // to print out the exception message
            e.printStackTrace();
        } finally {
            // close the db connections
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    static void createjob(String id, String type) {
        {
//it's parameter has id='ak#10' and type='Recruiter'
            String Id = "", Job_ID, Company, Description;
            Scanner input = new Scanner(System.in);

            System.out.println("Please enter your Job_ID");
            Job_ID = input.nextLine();

            System.out.println("Please enter your Company");
            Company = input.nextLine();

            System.out.println("Please enter your Description ");
            Description = input.nextLine();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            String created_on = dtf.format(now);

            try {
                //create a connection with url, id and password
                conn = DriverManager.getConnection(url, db_id, db_psw);
                // create a statement to do a query
                st = conn.createStatement();
                //we can ask user to enter id, pwd, Type, compay..
                //to save time , we just use hardcoding

                int rs = st.executeUpdate("insert into job_ad values('" + Job_ID + "','" + Company + "','" + Description + "','" + id + "','" + created_on + "')");
                accountcreator.shareajobad(id, Job_ID);
            } catch (SQLException e) {
                // to print out the exception message
                e.printStackTrace();
            } finally {
                // close the db connections
                try {
                    conn.close();
                    st.close();
                    //rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    static void shareajobad(String id, String Job_ID) {

        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();
            //we can ask user to enter id, pwd, Type, compay..
            //to save time , we just use hardcoding
            int rs = st.executeUpdate("insert into job_share values('" + Job_ID + "','" + id + "')");
            if (rs == 1) {
                System.out.println("Job Shared successfully");
            }
        } catch (SQLException e) {
            // to print out the exception message
            e.printStackTrace();
        } finally {
            // close the db connections
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static ArrayList<String> newconnections(String id) {
        // code to be executed
        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();

            ArrayList<String> dispc = new ArrayList<String>();
            rs = st.executeQuery(" SELECT * FROM profile where id in "
                    + "("
                    + "(select Receiver_ID from connection where Sender_ID= '" + id + "' and status = 'new') "
                    + "union "
                    + "(select Sender_ID from connection where Receiver_ID= '" + id + "' and status = 'new') "
                    + ") "
                    + "and id != '" + id + "'"
            );

            while (rs.next()) {
                dispc.add(rs.getString("Name") + " from " + rs.getString("Company") + " ," + rs.getString("Id"));
            }
            return dispc;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            //close the database
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    static void approveordeny(String senderid, String receiverid, String Status) {
        //code to be executed

        try {
            //create a connection with url, id and password
            conn = DriverManager.getConnection(url, db_id, db_psw);
            // create a statement to do a query
            st = conn.createStatement();
            //we can ask user to enter id, pwd, Type, compay..
            //to save time , we just use hardcoding
//            accountcreator.displayconnections(id, Boolean.TRUE);
            int rs;
            if (Status.equals("approve")) {
                rs = st.executeUpdate("UPDATE connection SET Status='approve' WHERE (Sender_ID='" + senderid + "' and Receiver_ID='" + receiverid + "')or ( Receiver_ID='" + receiverid + "' and Sender_ID='" + senderid + "' )");
                if (rs == 1) {
                    System.out.println("Connection Approved !!");
                } else {
                    System.out.println("Request  Failed !!");
                }
            } else {
                rs = st.executeUpdate("UPDATE connection SET Status='deny' WHERE (Sender_ID='" + senderid + "' and Receiver_ID='" + receiverid + "')or ( Receiver_ID='" + receiverid + "' and Sender_ID='" + senderid + "' )");
                if (rs == 1) {
                    System.out.println("Connection Denied !!");
                } else {
                    System.out.println("Request  Failed !!");
                }
            }

            System.out.println(rs);
        } catch (SQLException e) {
            // to print out the exception message
            e.printStackTrace();
        } finally {
            // close the db connections
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
