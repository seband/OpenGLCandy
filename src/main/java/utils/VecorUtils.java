package utils;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Help functions for vector utils
 */
public class VecorUtils {
    /**
     * Generates LookAtMatrix based on vector v
     * @param p camera position
     * @param l Point to look at
     * @param v Up vector
     * @return LookAtMatrix
     */
    public static Matrix4f lookAtv(Vector3f p, Vector3f l, Vector3f v)
    {
        Vector3f n, u;
        Matrix4f rot, trans;
        n = p.sub(l).normalize();
        u = v.cross(n).normalize();
        v = n.cross(u);

        rot = new Matrix4f(u.x, u.y, u.z, 0,
                v.x, v.y, v.z, 0,
                n.x, n.y, n.z, 0,
                0,   0,   0,   1);

        trans = T(-p.x, -p.y, -p.z);
        return rot.mul(trans);
    }

    /**
     * Generate translate matrix
     * @param tx Translation in x-axis
     * @param ty Translation in y-axis
     * @param tz Translation in z-axis
     * @return translation matrix
     */
    public static Matrix4f T(float tx, float ty, float tz)
    {
        Matrix4f m = new Matrix4f().identity();
        m._m13(tx);
        m._m23(ty);
        m._m33(tz);
        return m;
    }

    /**
     * Generate perspective matrix
     * @param fovyInDegrees field of view
     * @param aspectRatio aspect ratio of viewport
     * @param znear near plane
     * @param zfar far plane
     * @return perspective matrix
     */
    public static Matrix4f perspective(float fovyInDegrees, float aspectRatio, float znear, float zfar)
    {
        float ymax, xmax;
        ymax = znear * (float)Math.tan(fovyInDegrees * Math.PI / 360.0);
        if (aspectRatio < 1.0)
        {
            ymax = znear *(float)Math.tan(fovyInDegrees * Math.PI  / 360.0);
            xmax = ymax * aspectRatio;
        }
        else
        {
            xmax = znear *(float)Math.tan(fovyInDegrees * Math.PI  / 360.0);
            ymax = xmax / aspectRatio;
        }

        return frustum(-xmax, xmax, -ymax, ymax, znear, zfar);
    }

    /**
     * Generate frustum based on parameters
     * @param left left plane
     * @param right right plane
     * @param bottom bottom plane
     * @param top top plane
     * @param znear far plane
     * @param zfar near plane
     * @return frustum matrix
     */
    public static Matrix4f frustum(float left, float right, float bottom, float top, float znear, float zfar)
    {
        float temp, temp2, temp3, temp4;
        Matrix4f matrix = new Matrix4f();

        temp = 2.0f * znear;
        temp2 = right - left;
        temp3 = top - bottom;
        temp4 = zfar - znear;
        matrix._m01( temp / temp2); // 2*near/(right-left)
        matrix._m01(0.0f);
        matrix._m02(0.0f);
        matrix._m03(0.0f);
        matrix._m10(0.0f);
        matrix._m11(temp / temp3); // 2*near/(top - bottom)
        matrix._m12(0.0f);
        matrix._m13(0.0f);
        matrix._m20((right + left) / temp2); // A = r+l / r-l
        matrix._m21((top + bottom) / temp3); // B = t+b / t-b
        matrix._m22((-zfar - znear) / temp4); // C = -(f+n) / f-n
        matrix._m23(-1.0f);
        matrix._m30(0.0f);
        matrix._m31(0.0f);
        matrix._m32((-temp * zfar) / temp4); // D = -2fn / f-n
        matrix._m33(0.0f);

        return matrix;
    }

}
