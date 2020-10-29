/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkeduhcl_project;

/**
 *
 * @author darsh
 */
public  class Connection {
    String Sender_ID, Receiver_ID , status, msg , dateAndTime;
     public  Connection(String Sender_ID, String Reciever_ID, String status, String msg, String dateAndTime) {

        this.Sender_ID = Sender_ID;
        this.Receiver_ID = Reciever_ID;
        this.status = status;
         this.msg = msg;
          this.dateAndTime = dateAndTime;

    }

   
    
}
