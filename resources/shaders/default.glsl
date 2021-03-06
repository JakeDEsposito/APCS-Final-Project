#type vertex
#version 330 core

layout(location=0) in vec3 aPos;
layout(location=1) in vec4 aColor;
layout(location=2) in vec2 aTexCoords;
layout(location=3) in float aTexID;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;

out float fPlayerRot;

uniform mat4 uProjection;
uniform mat4 uView;

void main()
{
	gl_Position = uProjection * uView * vec4(aPos, 1.0);
	
	fColor = aColor;
	fTexCoords = aTexCoords;
	fTexID = aTexID;
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexID;

out vec4 color;

uniform sampler2D uTextures[8];

void main()
{
	if(fTexID > 0)
	{
		int id = int(fTexID);
		color = fColor * texture(uTextures[id], fTexCoords);
	}
	else
	{
		color = fColor;
	}
}