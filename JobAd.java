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
public class JobAd {
    public String Job_ID;
    public String Company;
    public String Description;
   
    public JobAd(String Job_ID, String Company, String Description) {

        this.Job_ID = Job_ID;
        this.Company = Company;
        this.Description = Description;
        

    }
     @Override
    public String toString(){
        return Description ;
    }
    
}
