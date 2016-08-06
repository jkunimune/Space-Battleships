/**
 * Copyright (c) 2016 Kunimune
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mechanics;

/**
 * An object with position and appearance, but no physical limitations
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class AbstractBody implements Body {

	@Override
	public String spriteName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double xValAt(double t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double yValAt(double t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object soundName(double t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] spriteTransform(double t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean doesScale() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existsAt(double t) {
		// TODO Auto-generated method stub
		return false;
	}

}
