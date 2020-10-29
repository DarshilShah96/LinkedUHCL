/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    package linkeduhcl_project;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 *
 * @author darsh
 */
public class LinkedUHCLProject {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner input = new Scanner(System.in);
        System.out.println("please select one ");
        System.out.println("1:signup");
        System.out.println("2:login");
        //get input from user
        String userInput = input.nextLine();
        if (userInput.equals("1")) {
            //sign up
            accountcreator.signup();

        } else {
            //log in
            String s = accountcreator.login();

            String[] strarr = s.split(",");

            if (strarr.length > 1) {
                Profile user = new Profile(strarr[0], strarr[1], strarr[2], strarr[3]);

                System.out.println("\nSome recommendations are : ");
                ArrayList<String> recom= accountcreator.displayrecommendations(user.getid(), user.getCompany());
                for (String a : recom) {
                    System.out.println(a);
                }
                
                System.out.println("\n Job Ads are : ");
   ArrayList<String> ads = accountcreator.displayJobAds(user.getid());
                for (String a : ads) {
                    System.out.println(a);
                }
                String selection = "";
                while (!selection.equals("x")) {
                    //display the menu
                    System.out.println("Please make your selection");
                    System.out.println("1: See  connected people and their profiles");
                    System.out.println("2: send a request to create connection");
                    if (user.getType().equals("Regular")) {
                        System.out.println("3: Share a job ad to connected people");

                    } else {
                        System.out.println("3:  create a job");
                    }

                    System.out.println("4: view new connection requests");

                    System.out.println("x: Finish the simulation");
                    selection = input.nextLine();
                    //get the selection from the user

                    if (selection.equals("1")) {
                        //See  connected people and their profiles
                        ArrayList<String> firstConnProfile = accountcreator.seeMyConnections(user.getid(), true);

                        while (true) {

                            int i = 1;
                            for (String p : firstConnProfile) {
                                System.out.println(i + " : " + p.toString());
                                i++;
                            }
                            System.out.println("Select any one from above to view their  profile and connections :");
                            int num = 0;
                            try {
                                num = Integer.parseInt(input.nextLine());
                                if (num <= firstConnProfile.size() && num > 0) {
                                    String[] str1= firstConnProfile.get(num - 1).split(",");
                                    String id = str1[1];
                                    accountcreator.seeMyConnections(id, false);
                                } else {
                                    break;
                                }
                            } catch (Exception e) {
                                break;
                            }
                        }
                    } else if (selection.equals("2")) {
                        //send a request to create connection
                        while (true) {
                            recom = accountcreator.displayrecommendations(user.getid(), user.getCompany());
                            System.out.println("Please choose any of the below to send a connection request! :");
                            int i = 1;
                            for (String a : recom) {
                                System.out.println(i + " :" + a);
                                i++;
                            }

                            int num = 0;
                            try {
                                num = Integer.parseInt(input.nextLine());
                                if (num <= ads.size() && num > 0) {
                                    String[] rec_user = ads.get(num - 1).split(",");
                                   
                                    accountcreator.sendConnectionRequest(user.getid(), rec_user[1]);
                                } else {
                                    break;
                                }
                            } catch (Exception e) {
                                break;
                            }
                            System.out.println("Select any one to view his/her profile and connections");

                            break;
                        }

                    } else if (selection.equals("3")) {
//                     Share and create a job ad to connected people
                        if (user.getType().equals("Recruiter")) {
                            accountcreator.createjob(user.getid(), user.getType());

                        } else {
                            ArrayList<String> jshare = accountcreator.displayJobAds(user.getid());
                            int i = 1;
                            for (String jsh : jshare) {
                                System.out.println(i + " :" + jsh);
                                i++;

                            }

                            System.out.println("select a job ad to share:");

                            int sel = Integer.parseInt(input.nextLine());
                             String[] str3= jshare.get(sel - 1).split(",");
                                String jobid = str3[1];
                                
                            accountcreator.shareajobad(user.getid(), jobid);
                            
                        }

                    } else if (selection.equals("4")) {
                        // if user wants to see new connection requests

                        ArrayList<String> newconn = accountcreator.newconnections(user.getid());

                        int i = 1;
                        System.out.println("Select any one from below to approve or deny :");

                        for (String appr : newconn) {
                            System.out.println(i + " : " + appr);
                            i++;
                        }
                        
                        int num = 0;
                        try {
                            num = Integer.parseInt(input.nextLine());
                            if (num <= newconn.size() && num > 0) {
                                String[] str2= newconn.get(num - 1).split(",");
                                String req_id = str2[1];
                                
                                System.out.println("please select one ");
                                System.out.println("1:Approve");
                                System.out.println("2:Deny");

                                String Input = input.nextLine();

                                if (Input.equals("1")) {
                                    //sign up
                                    accountcreator.approveordeny(req_id, user.getid(), "approve");

                                } else {
                                    accountcreator.approveordeny(req_id, user.getid(), "deny");

                                }
                            }
                        } catch (Exception e) {
                            break;
                        }

                    }

                }

            } else {
                System.out.println("The login failed");
            }
        }

    }
}

//if class does not have static we need to call it via object.methodname() if it has static then class.methodname()
