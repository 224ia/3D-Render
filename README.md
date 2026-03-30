# 3D Renderer Engine

A modular 3D renderer engine with abstract rendering backend. Currently implements a complete software renderer, designed to support OpenGL and other backends.

## Features

- **Abstract Renderer API** – Renderer interface allows swapping rendering backends
- **Software Renderer** – Complete CPU-based implementation with:
  - Triangle rasterization (barycentric coordinates)
  - Z-buffer depth testing
  - Perspective-correct texture mapping
  - OBJ model loading (positions, normals, UVs)
  - PNG texture loading
  - Diffuse lighting
- **First-person Camera** – WASD + mouse controls, FOV adjustment
- **UI System** – Abstract UI with text labels
- **Scene Management** – Objects, transforms, scene graph foundation

## Controls

| Key | Action |
|-----|--------|
| `WASD` | Move camera |
| `Q/E` | Move up/down |
| `Mouse` | Look around |
| `Scroll` | Zoom (FOV) |
| `ESC` | Toggle mouse lock |

## How to Run (Software Renderer)

```bash
./gradlew run
```
## Switching Renderers

    // Software renderer
    Renderer renderer = new SoftwareRenderer(width, height);

    // Future OpenGL renderer
    // Renderer renderer = new OpenGLRenderer(width, height);

    Engine engine = new Engine(renderer, width, height);

## Dependencies

    JOML – Matrix/vector math

    Java 17+

## Next Steps

    OpenGL renderer implementation

    Scanline optimization for software renderer

    Clipping

    Multiple textures per model

    Lighting system (multiple lights, attenuation)
