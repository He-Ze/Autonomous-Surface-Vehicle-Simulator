#version 130
precision mediump float;

uniform sampler2D texture_diffuse;
uniform vec4 colour;

in vec2 pass_TextureCoord;

out vec4 out_Color;

void ignoreTransparency(vec4 textureColour) {
	if (textureColour.a < 0.5){
		discard;
	}
}

void main(void) {
	out_Color = colour;
}