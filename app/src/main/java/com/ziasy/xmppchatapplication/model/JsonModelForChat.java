package com.ziasy.xmppchatapplication.model;

public class JsonModelForChat {
    boolean itemSelect =false;

    public boolean isItemSelect() {
        return itemSelect;
    }

    public void setItemSelect(boolean itemSelect) {
        this.itemSelect = itemSelect;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    String message;
    String sendName;
    String recieverName;
    String date;
    String id;
    String isread;
    String response;
    String time;
    public boolean isSelect = false;
    String deliver;
    private String posting;
    String mode;
    String status;
    String uploading;
    String did;
    String extension;
    String parent;
    int list_position;
    String group_name;
    String group_img;
    String group_desc;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_img() {
        return group_img;
    }

    public void setGroup_img(String group_img) {
        this.group_img = group_img;
    }

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }

    public int getList_position() {
        return list_position;
    }

    public void setList_position(int list_position) {
        this.list_position = list_position;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getUploading(){
        return uploading;
    }

    public void setUploading(String uploading) {
        this.uploading = uploading;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String mid, data, type, docName, uname, heading, timedate;

    public String getResponse() {
        return response;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public JsonModelForChat() {

    }

    public JsonModelForChat(String message, String sendName, String recieverName,
                            String date, String isread, String response, String time,
                            String deliver, String mid, String data, String type,
                            String docName, String uname, String heading, String timedate,
                            boolean isSelect, String uploading , String did, String extension,
                            String parent, int list_position) {
        this.message = message;
        this.sendName = sendName;
        this.recieverName = recieverName;
        this.date = date;
        this.isread = isread;
        this.response = response;
        this.time = time;
        this.deliver = deliver;
        this.mid = mid;
        this.data = data;
        this.type = type;
        this.docName = docName;
        this.uname = uname;
        this.heading = heading;
        this.isSelect = isSelect;
        this.timedate = timedate;
        this.uploading=uploading;
        this.did= did;
        this.extension=extension;
        this.parent=parent;
        this.list_position=list_position;
    }

    public JsonModelForChat(String message, String sendName, String recieverName,
                            String date, String isread, String response, String time,
                            String deliver, String mid, String data, String type,
                            String docName, String lang, String heading, String timedate,
                            boolean isSelect, String uploading , String did, String extension,
                            String parent, int list_position, String group_img, String group_desc, String group_name) {
        this.message = message;
        this.sendName = sendName;
        this.recieverName = recieverName;
        this.date = date;
        this.isread = isread;
        this.response = response;
        this.time = time;
        this.deliver = deliver;
        this.mid = mid;
        this.data = data;
        this.type = type;
        this.docName = docName;
        this.uname = lang;
        this.heading = heading;
        this.isSelect = isSelect;
        this.timedate = timedate;
        this.uploading=uploading;
        this.did= did;
        this.extension=extension;
        this.parent=parent;
        this.list_position=list_position;
        this.group_img=group_img;
        this.group_desc=group_desc;
        this.group_name=group_name;
    }

    public JsonModelForChat(String message, String sendName, String recieverName,
                            String date, String isread, String response, String time,
                            String deliver, String mid, String data, String type,
                            String docName, String lang, String heading, String timedate,
                            boolean isSelect) {
        this.message = message;
        this.sendName = sendName;
        this.recieverName = recieverName;
        this.date = date;
        this.isread = isread;
        this.response = response;
        this.time = time;
        this.deliver = deliver;
        this.mid = mid;
        this.data = data;
        this.type = type;
        this.docName = docName;
        this.uname = lang;
        this.heading = heading;
        this.isSelect = isSelect;
        this.timedate = timedate;
    }

    @Override
    public String toString() {
        return "\n"+"mid :"+id+"\n"+"Name :"+ sendName+" \n"+"recieverName : "+recieverName+" \n"+
                "timedate :"+timedate+" \n"+" message : "+message+"\n"+ "isread : "+isread+"\n"+"deliver : "+deliver
                +"\n"+"type : "+type+"\n"+ "time : "+time+"\n"+"date : "+date+"\n"+" UID : "+mid;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimedate() {
        return timedate;
    }

    public void setTimedate(String timedate) {
        this.timedate = timedate;
    }

    public String getData() {

        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
