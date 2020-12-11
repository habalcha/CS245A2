public class LinkedList<String>{

    node head;
    int size = 0;

    // constructor
    public LinkedList(){} // end constructor

    public LinkedList(String item){
        add(item); // add item to linkedlist
    }

    // node
    class node {
        String data;
        node next;

        public node(String item){
            data = item;
            next = null;
        }
    } // end node


    // add item to end of linkedlist
    public void add(String item){
        // no items in list
        if (head == null){
            head = new node(item);
            ++ size;
        }

        else{
            node n = new node(item);
            node prev = head;

            // get to last filled node
            while(prev.next != null){
                prev = prev.next;
            }

            prev.next = n;
            ++size;
        }
    } // end add


    // add item to a specific point of linkedlist
    public void add(int pos, String item){
        try {
            if (pos > size || pos < 0) {
                throw new Exception();
            }
        }
        catch (Exception e){
            System.out.println ("Position is not valid.");
        }

        node n = new node(item);

        // change head if pos = 0
        if (pos == 0){
            n.next = head;
            head = n;
            ++size;
        } else {
            node prev = head;
            for (int i = 0; i < pos-1; i++){
                prev = prev.next;
            }

            n.next = prev.next;
            prev.next = n;
            ++ size;
        }
    } // end add


    // get node at pos and return the data
    public String get(int pos){
        try {
            if (pos > size || pos < 0) {
                throw new Exception();
            }
        }
        catch (Exception e){
            System.out.println ("Position is not valid.");
        }

        if (pos == 0){
            return head.data;
        }

        node val = head;
        while (val.next != null){
            val = val.next;
        }
        return val.data;
    }

    public boolean hasValue(String val){

        node prev = head;
        for (int i = 0; i < size-1; i++) {

            if (val.equals(prev.data)){
                return true;
            }

            prev = prev.next;
        }

        return false;
    }


    public String remove(){
        return remove(0);
    }


    public String remove(int pos){
        try {
            if (pos > size || pos < 0) {
                throw new Exception();
            }
        }
        catch (Exception e){
            System.out.println ("Position is not valid.");
        }

        if (pos == 0){
            node n = head;
            head = head.next;
            -- size;
            return n.data;
        }
        else {
            node prev = head;
            for (int i = 0; i < pos-1; i++) {
                prev = prev.next;
            }
            node n = prev.next;
            if (n.next != null){
                prev.next = n.next;
            }
            --size;
            return n.data;
        }
    }

    public int size(){
        return size;
    }
}