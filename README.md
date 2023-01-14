# Fractal Explorer
Slow but cool mandelbrot and julia set explorer in java.

## Controls
- Zoom in: 0
- Zoom out: 9
- Move left/right: Left Arrow / Right Arrow
- Move up/down: Up Arrow / Down Arrow

### Control the set
- Increase/Decrease iterations: D / A
- Switch from Mandelbrot set to Julia set: S
- Cycle between 3 color options: C
  - (Random (pre - made), Black and White, Rainbow

## How the Julia set functionality works
The julia set constants will be based on the MouseX and MouseY respective to the window.  
The real constant is based on the MouseX and the imaginary constant is based on the MouseY.  

If you want to change the Random (pre - made) colors, you can change in the getColor function:
    case 0:
    float a = (iterations == -1) ? -1f : iterations / 1000f + .5f;
    return getHSBColor((a == -1) ? .35f : a, iterations, a);
Yeah.. its a bit messy but just mess around with the numbers and really cool colors can pop out!  
Hvae fun.


