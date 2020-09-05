#version 130

uniform sampler2D texture_diffuse;
uniform vec4 colour;

in vec2 pass_TextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;
in float lightDistance;

out vec4 out_Color;

void main(void) {
	vec4 textureColour = texture2D(texture_diffuse, pass_TextureCoord);
	if (textureColour.a < 0.5){
		discard;
	}

	out_Color = colour;
}