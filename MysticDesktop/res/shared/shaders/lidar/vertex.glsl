#version 130

uniform mat4 projectionRotationMatrix;
uniform vec4 viewPos;
uniform vec4 modelPos;
uniform vec4 modelSize;
uniform vec4 modelRot;
uniform vec4 clipPlane;

in vec4 in_Position;
in vec2 in_TextureCoord;
in vec3 normal;

void main(void) {
    ///*
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
	mat4 rotMatrix = modelRotXAxis * modelRotYAxis * modelRotZAxis;
	//*/
    /*
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
	
	mat4 rotMatrix = rotXZMat * rotYZMat; //TODO move this calculation out of here and make it a uniform
    */
	vec4 modelOrientation = rotMatrix * (in_Position * modelSize);
	vec4 worldPosition = modelOrientation + modelPos;

	gl_ClipDistance[0] = dot(worldPosition, clipPlane);
	
	gl_Position =  projectionRotationMatrix * (worldPosition - viewPos);
}