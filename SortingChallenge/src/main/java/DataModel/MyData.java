package Model;

public class MyData implements Comparable<MyData>{

    private int key;
    private int referenceVal;

    public MyData(int key, int referenceVal){
        this.key = key;
        this.referenceVal = referenceVal;
    }

    public int getKey(){
        return this.key;
    }

    public int getReferenceVal(){
        return this.referenceVal;
    }

    @Override
    public int compareTo(MyData compData){
        int compKey = compData.getKey();
        return Integer.compare(this.key, compKey);
        //return compKey == 0 ? this.key.compareTo(compData.getKey()) : compKey;
    }

    @Override
    public String toString(){
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Data [ ")
                .append("key=").append(this.key)
                .append(", referenceVal=").append(this.referenceVal)
                .append("]");
        return stringBuilder.toString();
    }

}
