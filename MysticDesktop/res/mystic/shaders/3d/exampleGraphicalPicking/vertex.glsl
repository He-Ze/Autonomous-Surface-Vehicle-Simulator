#version 130
precision mediump float;

uniform mat4 projectionRotationMatrix;
uniform vec4 viewPos;
uniform vec4 modelPos;
uniform vec4 modelSize;
uniform vec4 modelRot;
uniform vec2 textureOffset;
uniform vec4 uniformScale;

in vec4 in_Position;
in vec2 in_TextureCoord;
in vec3 normal;

out vec2 pass_TextureCoord;

void main() {
    mat4 modelRotXAxis = mat4(
		1, 0, 0, 0,
		0, cos(modelRot.x), -sin(modelRot.x), 0,
		0, sin(modelRot.x), cos(modelRot.x), 0,
		0, 0, 0, 1
	);

	mat4 modelRotYAxis = mat4(
		cos(modelRot.y), 0, sin(modelRot.y), 0,
		0, 1, 0, 0,
		-sin(modelRot.y), 0, cos(modelRot.y), 0,
		0, 0, 0, 1
	);

	mat4 modelRotZAxis = mat4(
		cos(modelRot.z), -sin(modelRot.z), 0, 0,
		sin(modelRot.z), cos(modelRot.z), 0, 0,
		0, 0, 1, 0,
		0, 0, 0, 1
	);
	mat4 rotMatrix = modelRotYAxis * modelRotXAxis * modelRotZAxis;
    /*
	mat4 rotXZMat = mat4(
		cos(modelRot.z), -sin(modelRot.z), 0, 0,
		0, cos(modelRot.x), -sin(modelRot.x), 0,
		0, sin(modelRot.x), cos(modelRot.x), 0,
		0, 0, 0, 1
	);

	mat4 rotYZMat = mat4(
		cos(modelRot.y), 0, sin(modelRot.y), 0,
		sin(modelRot.z), cos(modelRot.z), 0, 0,
		-sin(modelRot.y), 0, cos(modelRot.y), 0,
		0, 0, 0, 1
	);
	mat4 rotMatrix = rotXZMat * rotYZMat;
	*/

	vec4 modelOrientation = rotMatrix * (in_Position * modelSize);
    vec4 worldPosition = modelOrientation + modelPos;
    gl_Position =  projectionRotationMatrix * (worldPosition - viewPos);

	pass_TextureCoord = in_TextureCoord + textureOffset;
}
