#version 130

uniform mat4 projectionRotationMatrix;
uniform vec4 viewPos;
uniform vec3 lightPosition;
uniform vec4 modelPos;
uniform vec4 modelSize;
uniform vec4 modelRot;
uniform vec2 textureOffset;
uniform vec4 uniformScale; //must be set

in vec4 in_Position;
in vec2 in_TextureCoord;
in vec3 normal;

out vec2 pass_TextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector;
out float lightDistance;

void main(void) {
	
	
	//TODO: modelRotation
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
	
	//Slightly faster - But not as much of an increase as you would think.
		
	vec4 modelOrientation = (modelRotYAxis * (modelRotXAxis * (modelRotZAxis * (in_Position * modelSize))));
	vec4 worldPosition = modelOrientation + modelPos - viewPos;
	
	gl_Position =  projectionRotationMatrix * worldPosition * uniformScale;
	
	pass_TextureCoord = in_TextureCoord + textureOffset;
	
	surfaceNormal = (modelRotYAxis * (modelRotXAxis * (modelRotZAxis * vec4(normal, 0.0)))).xyz;
	toLightVector = (lightPosition - viewPos.xyz) - worldPosition.xyz;
	
	lightDistance = length(toLightVector);
}