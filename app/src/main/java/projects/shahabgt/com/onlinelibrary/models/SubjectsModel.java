package projects.shahabgt.com.onlinelibrary.models;

public class SubjectsModel {
    private String id;
    private String name;
    private String count;
    private String price;

    public String get_id(){
        return id;
    }
    public String get_name(){
        return name;
    }
    public String get_count(){
        return count;
    }
    public String get_price(){
        return price;
    }

    public void set_id(String id){
        this.id = id;
    }
    public void set_name(String name){
        this.name = name;
    }
    public void set_count(String count){
        this.count = count;
    }
    public void set_price(String price){
        this.price = price;
    }

}
