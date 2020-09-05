#version 130
//version 150 core

uniform sampler2D texture_diffuse;

in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
	
	vec4 textureColour = texture2D(texture_diffuse, pass_TextureCoord);
	
	vec4 sumColour = vec4(0);
	for(int i = -2; i <= 2; i++){
		for(int j = -2; j <= 2; j++){
			vec2 offset = vec2(i, j) * 0.0005;
			sumColour += texture2D(texture_diffuse, pass_TextureCoord + offset);
		}
	}
	
	//out_Color = sumColour / 25 + textureColour;
	vec4 finalColour = sumColour / 25;
	//finalColour.a = sumColour.a / 3;
	finalColour.a = 0.5;
	out_Color = finalColour;
}