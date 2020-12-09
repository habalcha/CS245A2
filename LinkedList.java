public class LinkedList{

    node head;
    int size = 0;

    public LinkedList(){}

    public LinkedList(String item){
        add(item);
    }

    class node {
        String data;
        node next;

        public node(String item){
            data = item;
            next = null;
        }
    }

    public void add(String item){
        if (head == null){
            head = new node(item);
            ++ size;
        }

        else{
            node n = new node(item);
            node prev = head;

            while(prev.next != null){
                prev = prev.next;
            }

            prev.next = n;
            ++size;
        }
    }

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
        if (pos == 0){
            n.next = head;
            head = n;
            ++size;
            }
        else{
            node prev = head;
            for (int i = 0; i < pos-1; i++){
                prev = prev.next;
            }

            n.next = prev.next;
            prev.next = n;
            ++ size;
        }
    }

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