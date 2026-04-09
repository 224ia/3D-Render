#version 330 core

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

//uniform sampler2D ourTexture;
//uniform vec3 lightDir;
//uniform vec3 lightColor;
//uniform vec3 objectColor;

vec3 lightDir = vec3(-1, 0, 1);

out vec4 FragColor;

void main() {
    vec3 norm = normalize(Normal);
    float diff = max(dot(norm, lightDir) * 0.5 + 0.2, 0.0);
    vec3 ambient = vec3(0.1);
    vec3 diffuse = diff * vec3(1) + ambient;

    vec3 result = diffuse;
    FragColor = vec4(diffuse, 1.0);
}