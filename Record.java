public class Record{
    public int a, b, c, d, e, f, g, h;
   
    public Record(int a, int b, int c, int d, int e, int f, int g, int h) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
    }

    public void displayRecord() {
        System.out.println("Selected Record: a=" +
                this.getA() + ", b=" +
                this.getB() + ", c=" +
                this.getC() + ", d=" +
                this.getD() + ", e=" +
                this.getE() + ", f=" +
                this.getF() + ", g=" +
                this.getG() + ", h=" +
                this.getH());
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public int getD() {
        return d;
    }

    public int getE() {
        return e;
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }
}