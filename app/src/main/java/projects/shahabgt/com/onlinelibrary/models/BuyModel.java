package projects.shahabgt.com.onlinelibrary.models;

public class BuyModel {


    private String subject;
    private String price;
    private String date;
    private String rescode;

    public String get_rescode() {
        return rescode;
    }

    public void set_rescode(String rescode) {
        this.rescode = rescode;
    }

    public String get_subject(){
        return subject;
    }
    public String get_price(){
        return price;
    }
    public String get_date(){
        return date;
    }



    public void set_subject(String subject){
        this.subject = subject;
    }
    public void set_price(String price){
        this.price = price;
    }
    public void set_date(String date){
        this.date = date;
    }

}
