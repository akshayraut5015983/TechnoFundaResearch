package com.app.technofunda.shopping;

    public class CategoryList1 {


        //Data Variables
        private String imageUrl;
        private String name;
        private String id;
        private String bodposition;
        private String productid;
        private String eventname;
        private String price;
        private String discount;
        private String sellprice;

        public String getProductid() {
            return productid;
        }

        public void setProductid(String productid) {
            this.productid = productid;
        }

        public String getEventname() {
            return eventname;
        }

        public void setEventname(String eventname) {
            this.eventname = eventname;
        }

        public String getEventdate() {
            return eventdate;
        }

        public void setEventdate(String eventdate) {
            this.eventdate = eventdate;
        }

        public String getEventdescription() {
            return eventdescription;
        }

        public void setEventdescription(String eventdescription) {
            this.eventdescription = eventdescription;
        }

        private String eventdate;
        private String eventdescription;

        public String getBodposition() {
            return bodposition;
        }

        public void setBodposition(String bodposition) {
            this.bodposition = bodposition;
        }

        //Getters and Setters
        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setDisc(String discount) {
            this.discount = discount;

        }
        public String getDisc() {
            return discount;
        }


        public void setSellPrice(String sellprice) {
            this.sellprice =sellprice;
        }

        public String getSellPrice() {
            return sellprice;
        }


    }

