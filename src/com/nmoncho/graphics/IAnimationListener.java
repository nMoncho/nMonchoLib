package com.nmoncho.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;

public interface IAnimationListener {

	void onAnimationEnd(String currentAnimName, Animation currentAnim);
}
