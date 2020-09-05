#version 130

uniform mat4 projectionRotationMatrix;
uniform vec4 viewPos;
uniform vec4 modelPos;
uniform vec4 modelSize;
uniform vec4 modelRot;
uniform vec2 textureOffset;

in vec4 in_Position;
in vec2 in_TextureCoord;
in vec3 normal;

out vec2 pass_TextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;
out float lightDistance;


void main() {
	mat4 rotXZMat = mat4(
		cos(modelRot.y), -sin(modelRot.y), 0, 0,
		0, cos(modelRot.x), -sin(modelRot.x), 0,
		0, sin(modelRot.x), cos(modelRot.x), 0,
		0, 0, 0, 1
	);

	mat4 rotYZMat = mat4(
		cos(modelRot.z), 0, sin(modelRot.z), 0,
		sin(modelRot.y), cos(modelRot.y), 0, 0,
		-sin(modelRot.z), 0, cos(modelRot.z), 0,
		0, 0, 0, 1
	);

	mat4 rotMatrix = rotXZMat * rotYZMat;

	vec4 modelOrientation = rotMatrix * (in_Position * modelSize);
	vec4 worldPosition = modelOrientation + modelPos - viewPos;

	gl_Position =  projectionRotationMatrix * worldPosition;

	pass_TextureCoord = in_TextureCoord + textureOffset;
}
