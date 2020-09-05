package blindmystics.util.vector;

import javax.vecmath.Tuple2f;

/**
 * Created by CaptainPete on 9/6/2016.
 */
public class Matrix2f {

    public float m00;
    public float m01;
    public float m10;
    public float m11;

    private static final double EPS = 1.0E-8D;

    public Matrix2f(float var1, float var2, float var3, float var4) {
        m00 = var1;
        m01 = var2;
        m10 = var3;
        m11 = var4;
    }

    public Matrix2f() {
        this(0f, 0f, 0f, 0f);
    }

    @Override
    public String toString() {
        return this.m00 + ", " + this.m01 + "\n" + this.m10 + ", " + this.m11 + "\n";
    }

    public final void add(float var1) {
        this.m00 += var1;
        this.m01 += var1;
        this.m10 += var1;
        this.m11 += var1;
    }

    public final void add(Matrix2f var1) {
        this.m00 += var1.m00;
        this.m01 += var1.m01;
        this.m10 += var1.m10;
        this.m11 += var1.m11;
    }

    public final void sub(Matrix2f var1) {
        this.m00 -= var1.m00;
        this.m01 -= var1.m01;
        this.m10 -= var1.m10;
        this.m11 -= var1.m11;
    }

    public final void transpose() {
        float var1 = this.m10;
        this.m10 = this.m01;
        this.m01 = var1;
    }

    public final void mul(float var1) {
        this.m00 *= var1;
        this.m01 *= var1;
        this.m10 *= var1;
        this.m11 *= var1;
    }

    public final void mul(Matrix2f var1) {
        float var2 = this.m00 * var1.m00 + this.m01 * var1.m10;
        float var3 = this.m00 * var1.m01 + this.m01 * var1.m11;
        float var4 = this.m10 * var1.m00 + this.m11 * var1.m10;
        float var5 = this.m10 * var1.m01 + this.m11 * var1.m11;
        this.m00 = var2;
        this.m01 = var3;
        this.m10 = var4;
        this.m11 = var5;
    }

    public final void transform(Tuple2f var1) {
        float var2 = this.m00 * var1.x + this.m01 * var1.y;
        float var3 = this.m10 * var1.x + this.m11 * var1.y;
        var1.set(var2, var3);
    }
}
