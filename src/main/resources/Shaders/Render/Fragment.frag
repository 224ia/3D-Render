#version 330 core

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

uniform sampler2D texture;
uniform bool useTexture;

uniform vec3 color;

uniform vec3 lightDir;

out vec4 FragColor;

void main() {
    vec3 norm = normalize(Normal);
    float diff = max(dot(norm, -lightDir) * 0.5 + 0.2, 0.0);
    vec3 ambient = vec3(0.1);
    vec3 diffuse = diff * vec3(1) + ambient;

    vec3 result = diffuse * color;

    if (useTexture) {
        vec4 texel = texture2D(texture, TexCoord);
        FragColor = vec4(result, 1.0) * texel;
    } else {
        FragColor = vec4(result, 1.0);
    }
}