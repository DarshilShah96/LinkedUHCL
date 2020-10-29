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
public class Profile {
    //three important variables for database operations
    private static String Id;
    private static String Company;
    private static String Type;
    private static String Name;

    public Profile(String Id, String Company, String Type, String Name) {

        this.Id = Id;
        this.Company = Company;
        this.Type = Type;
        this.Name = Name;

    }

    public String getid() {
        //getting the id String instance
        return Id;
    }

    public void setid(String Id) {
        //setting the id String value
        Id = Id;
    }


    public String getCompany() {
        //getting the Company variable instance
        return Company;
    }

    public void setCompany(String Company) {
        //setting the Company variable text
        Company = Company;
    }

    public String getType() {
        //getting the Type variable instance
        return Type;
    }

    public void setType(String Type) {
        //setting the Type variable value
        Type = Type;
    }

    public String getName() {
        //getting the Name variable
        return Name;
    }

    public void setName(String Name) {
        Name = Name;
    }

 

//    public void setShare(String Share) {
//        //setting the share variable
//        Share = Share;
//    }
//    

    @Override
    public String toString(){
        return this.getName()+" from "+this.getCompany();
    }
     
}
