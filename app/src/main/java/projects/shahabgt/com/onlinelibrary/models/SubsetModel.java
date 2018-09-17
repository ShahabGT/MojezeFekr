package projects.shahabgt.com.onlinelibrary.models;

public class SubsetModel {
    private String id;
    private String subject;
    private String title;
    private String s_id;

    public String get_id(){
        return id;
    }
    public String get_s_id(){
        return s_id;
    }
    public String get_subject(){
        return subject;
    }
    public String get_title(){
        return title;
    }


    public void set_id(String id){
        this.id = id;
    }
    public void set_s_id(String s_id){
        this.s_id = s_id;
    }
    public void set_subject(String subject){
        this.subject = subject;
    }
    public void set_title(String title){
        this.title = title;
    }


}
