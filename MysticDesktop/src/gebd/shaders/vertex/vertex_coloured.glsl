#version 130
//version 150 core

uniform mat4 projectionMatrix;
//uniform mat4 viewMatrix;
//uniform mat4 modelMatrix;

uniform vec4 viewPos;
uniform vec4 viewRot;

in vec4 in_Position;

in vec4 in_Color;
//in vec2 in_TextureCoord;

out vec4 pass_Color;
//out vec2 pass_TextureCoord;

void main(void) {
	// Probably want to compute these separately then just input the result instead of recalculating for each vertex
	
	mat4 viewRotXAxis = mat4(
		1, 0, 0, 0,
		0, cos(viewRot.y), -sin(viewRot.z), 0,
		0, sin(viewRot.y), cos(viewRot.z), 0,
		0, 0, 0, 1
	);
	
	mat4 viewRotYAxis = mat4(
		cos(viewRot.x), 0, sin(viewRot.z), 0,
		0, 1, 0, 0,
		-sin(viewRot.x), 0, cos(viewRot.z), 0,
		0, 0, 0, 1
	);
	
	mat4 viewRotZAxis = mat4(
		cos(viewRot.x), -sin(viewRot.y), 0, 0,
		sin(viewRot.x), cos(viewRot.y), 0, 0,
		0, 0, 1, 0,
		0, 0, 0, 1
	);
	
	mat4 viewRotXYZ = viewRotXAxis * viewRotYAxis * viewRotZAxis;
	

	gl_Position = (in_Position + viewPos) * viewRotXYZ * projectionMatrix;
	
	pass_Color = in_Color;
}