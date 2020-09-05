package gebd.camera.projection;

import javax.vecmath.Matrix4f;

/**
 * Created by p3te on 19/08/16.
 */
public class ProjectionMatrixHandler {

    float nearPlane;
    float farPlane;
    float fieldOfView;
    float aspectRatio;
    boolean isPerspective;

    Matrix4f projectionMatrix = new Matrix4f();
    Matrix4f invertedProjectionMatrix = new Matrix4f();

    public ProjectionMatrixHandler(float nearPlane, float farPlane, float fieldOfView, float aspectRatio, boolean isPerspective) {
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
        this.fieldOfView = fieldOfView;
        this.aspectRatio = aspectRatio;
        this.isPerspective = isPerspective;
        generateProjectionMatrix();
    }

    private void generateProjectionMatrix() {
        projectionMatrix.setZero();
        invertedProjectionMatrix.setZero();
        if (isPerspective) {
            generatePerspectiveProjectionMatrix();
        } else {
            generateOrthographicProjectionMatrix();
        }
        invertedProjectionMatrix.set(projectionMatrix);
        invertedProjectionMatrix.invert();
    }

    private void generatePerspectiveProjectionMatrix() {
        float y_scale = (float) (1 / Math.tan((Math.toRadians(fieldOfView / 2f)))); // (1/tan) == cotangent.
        float x_scale = y_scale / aspectRatio;
        float frustrum_length = farPlane - nearPlane;

        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -(farPlane + nearPlane) / frustrum_length;
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -(2 * nearPlane * farPlane) / frustrum_length;
        projectionMatrix.m33 = 0;
    }

    private void generateOrthographicProjectionMatrix() {
        projectionMatrix.m00 = 1; // 2/(r-l)
        projectionMatrix.m30 = 0; // -(r+l)/(r-l)
        projectionMatrix.m11 = aspectRatio; // 2/(t-b)
        projectionMatrix.m31 = 0; // -(t+b)/(t-b)
        projectionMatrix.m22 = -2/(farPlane-nearPlane); // -2/(f-n)
        projectionMatrix.m32 = 0; // -(f+n)/(f-n)
    }

    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
        generateProjectionMatrix();
    }

    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
        generateProjectionMatrix();
    }

    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
        generateProjectionMatrix();
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        generateProjectionMatrix();
    }

    public void setPerspective(boolean perspective) {
        isPerspective = perspective;
        generateProjectionMatrix();
    }

    public float getNearPlane() {
        return nearPlane;
    }

    public float getFarPlane() {
        return farPlane;
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public boolean isPerspective() {
        return isPerspective;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getInvertedProjectionMatrix() {
        return invertedProjectionMatrix;
    }
}
