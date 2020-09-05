package gebd.shaders;

import blindmystics.util.FileReader;
import blindmystics.util.GLWrapper;
import gebd.Render;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.util.vector.*;

import javax.vecmath.*;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

public abstract class Shader {
	
	protected int programId;
	protected int fragmentShaderId;
	protected int vertexShaderId;
	
	protected static FloatBuffer matrix44Buffer = BufferUtils.createFloatBuffer(16);
	protected static FloatBuffer matrix33Buffer = BufferUtils.createFloatBuffer(9);
	
	
	public Shader(int vertexShaderId, int fragmentShaderId){
		this.vertexShaderId = vertexShaderId;
		this.fragmentShaderId = fragmentShaderId;
	}
	
	public void initialize() {
		linkShaders();
		Render.getInstance().exitOnGLError("Link shaders");
		setupUniformVariables();
		Render.getInstance().exitOnGLError("Setup uniform variables");
	}
	
	protected abstract void setupUniformVariables();
	
	public abstract void prepare();
	/**
	 * For each shader, Create a new shader program that links vertex with fragment.
	 */
	protected void linkShaders(){
		programId = GLWrapper.glCreateProgram();
		GLWrapper.glAttachShader(programId, vertexShaderId);
		GLWrapper.glAttachShader(programId, fragmentShaderId);

		GLWrapper.glLinkProgram(programId);

		GLWrapper.glValidateProgram(programId);

		if (!GLWrapper.glIsProgram(programId)) {
			throw new OpenGLException("Could not compile shader.");
		}
		Render.getInstance().exitOnGLError("glIsProgram problems");

		if (GLWrapper.glGetProgrami(programId, GLWrapper.GL_LINK_STATUS) == GLWrapper.GL_FALSE) {
			throw new OpenGLException("Could not link shader.");
		}
		Render.getInstance().exitOnGLError("glGetProgrami link problems");

		bindAttributeLocations();
	}
	
	protected abstract void bindAttributeLocations();
	
	public int getProgramId() {
		return programId;
	}
	
	
	public static int loadShader(String filename, int type) {
		System.out.println(filename);
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(FileReader.asInputStream(filename)));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		shaderID = GLWrapper.glCreateShader(type);
		GLWrapper.glShaderSource(shaderID, shaderSource);
		GLWrapper.glCompileShader(shaderID);

		Render.getInstance().exitOnGLError("Shader problems");

		if (GLWrapper.glGetShaderi(shaderID, GLWrapper.GL_COMPILE_STATUS) == GLWrapper.GL_FALSE) {
			throw new OpenGLException("Could not compile shader.");
		}
		Render.getInstance().exitOnGLError("getShaderi problems");
		
		return shaderID;
	}
	
	
	protected int getUniformLocation(String variableName){
		return GLWrapper.glGetUniformLocation(programId, variableName);
	}

	protected void loadBool(int uniformLocation, boolean value) {
		if (value) {
			GLWrapper.glUniform1i(uniformLocation, 1);
		} else {
			GLWrapper.glUniform1i(uniformLocation, 0);
		}
	}
	
	protected void loadInt(int uniformLocation, int value){
		GLWrapper.glUniform1i(uniformLocation, value);
	}
	
	protected void loadFloat(int uniformLocation, float value){
		GLWrapper.glUniform1f(uniformLocation, value);
	}
	
	protected void loadVec2(int uniformLocation, Vector2f vec2){
		GLWrapper.glUniform2f(uniformLocation, vec2.x, vec2.y);
	}
	
	protected void loadVec2(int uniformLocation, float x, float y){
		GLWrapper.glUniform2f(uniformLocation, x, y);
	}

	protected void loadVec3(int uniformLocation, Vector3f vec3){
		GLWrapper.glUniform3f(uniformLocation, vec3.x, vec3.y, vec3.z);
	}
	protected void loadVec3(int uniformLocation, float x, float y, float z){
		GLWrapper.glUniform3f(uniformLocation, x, y, z);
	}
	
	protected void loadVec4(int uniformLocation, Tuple4f vec4){
		GLWrapper.glUniform4f(uniformLocation, vec4.x, vec4.y, vec4.z, vec4.w);
	}
	protected void loadVec4(int uniformLocation, Vector3f vec3, float w){
		GLWrapper.glUniform4f(uniformLocation, vec3.x, vec3.y, vec3.z, w);
	}

	protected void loadVec4(int uniformLocation, float x, float y, float z, float w){
		GLWrapper.glUniform4f(uniformLocation, x, y, z, w);
	}
	
	protected void loadMatrix4f(int uniformLocation, Matrix4f matrix4f){

		matrix44Buffer.put(matrix4f.m00);
		matrix44Buffer.put(matrix4f.m01);
		matrix44Buffer.put(matrix4f.m02);
		matrix44Buffer.put(matrix4f.m03);
		matrix44Buffer.put(matrix4f.m10);
		matrix44Buffer.put(matrix4f.m11);
		matrix44Buffer.put(matrix4f.m12);
		matrix44Buffer.put(matrix4f.m13);
		matrix44Buffer.put(matrix4f.m20);
		matrix44Buffer.put(matrix4f.m21);
		matrix44Buffer.put(matrix4f.m22);
		matrix44Buffer.put(matrix4f.m23);
		matrix44Buffer.put(matrix4f.m30);
		matrix44Buffer.put(matrix4f.m31);
		matrix44Buffer.put(matrix4f.m32);
		matrix44Buffer.put(matrix4f.m33);

		matrix44Buffer.flip();
		GLWrapper.glUniformMatrix4(uniformLocation, false, matrix44Buffer);
	}
	
	protected void loadMatrix3f(int uniformLocation, Matrix3f matrix3f){

		matrix33Buffer.put(matrix3f.m00);
		matrix33Buffer.put(matrix3f.m01);
		matrix33Buffer.put(matrix3f.m02);
		matrix33Buffer.put(matrix3f.m10);
		matrix33Buffer.put(matrix3f.m11);
		matrix33Buffer.put(matrix3f.m12);
		matrix33Buffer.put(matrix3f.m20);
		matrix33Buffer.put(matrix3f.m21);
		matrix33Buffer.put(matrix3f.m22);

		matrix33Buffer.flip();
		GLWrapper.glUniformMatrix4(uniformLocation, false, matrix33Buffer);
	}
	
	protected void loadFloatArray(int uniformLocation, FloatBuffer floatBuffer){
		GLWrapper.glUniform1(uniformLocation, floatBuffer);
	}

	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(obj instanceof Shader){
			Shader testShader = (Shader) obj;
			return (testShader.getProgramId() == getProgramId());
		}
		return false;
	}
	
}
