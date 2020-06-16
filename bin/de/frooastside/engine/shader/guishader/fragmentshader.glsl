#version 400

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec4 color;
uniform bool useColor;
uniform float brightness;

void main(void){

	if(useColor) {
		out_Color = color * brightness;
	}else {
		out_Color = texture(guiTexture, textureCoords) * brightness;
	}

}
