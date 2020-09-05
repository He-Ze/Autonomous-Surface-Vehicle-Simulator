#version 130
//version 150 core

uniform sampler2D texture_diffuse;
uniform vec4 mixColour;
uniform float mixAmount;

in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
	
	vec4 textureColour = texture2D(texture_diffuse, pass_TextureCoord);
	vec4 temp = mixColour;
	temp.w = textureColour.w * mixColour.w;
	out_Color = mix(textureColour, temp, mixAmount);
	
	
}