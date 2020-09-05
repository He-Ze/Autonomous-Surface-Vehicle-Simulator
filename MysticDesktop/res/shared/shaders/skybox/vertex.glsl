#version 130

uniform mat4 projectionRotationMatrix;

in vec3 position;
out vec3 textureCoords;

void main(void) {
	gl_Position =  projectionRotationMatrix * vec4(position, 1.0);
	textureCoords = position;
}