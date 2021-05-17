# PieChartView
A Customize piechart 

<img src="https://github.com/thanh-ha-van/PieChartView/blob/master/Untitled.png" width="500" height="500" />
# How to set up gradle: 

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.havanthanh-mfv:PieChartView:1.0.1'
	}

# Feature: 
## XML editing
  
            app:itemTextColor="@color/white" : Text color for a pie item.
            app:pieStrokeWidth="10dp" : The bigger this stroke, the pie fill more erea. 
            Until it can fix no more into its center. 
            If this value is bigger than a cetain half of chart size, 
            it will over lap chart. I'll fix it soon.
            app:initAngle="120" : Init angle to the 0 degree of native android system, 
            which is 0 degree in mathematic x-y coordination.
            app:shadowAlpha="0.2" : Shadow Alpha of piechart. 
            Working from 0.01 to 0.99. 
            Don't try with other value i don't know if it gonna crash. 
            app:animateDirection="clockwise" : Animate direction.
            app:shadowDx="0dp" : X shif position for chart shadow. 
            Chart will scale on it own to make sure the shadow is not cutted down by parent view. 
            So the bigger shadow Dx, Dx and Radius, the smaller chart is. 
            app:shadowDy="10dp" : Y shif position for chart shadow
            app:shadowRadius="10dp" : Radius for chart shadow
            app:textFontFamily="@font/noto_sans_jp_bold" : Pie text font
