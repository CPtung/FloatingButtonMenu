# FloatingButtonMenu

## Introduction

This project allows user to easily integrate a customed menu of floating buttons into your app. 
The following will take through several parts to introduce the customed FloatingButtonMenu.

## Layout Parameters

FloatingButton.java this class provides customed layout parameters for user to set in xml.
* labelText: Every menu button comes with a text label to show their function.
* labelBackgroundColor: Change the label background color.
* fabType: As Google defined, floating button can a normal or mininal size one.
* fabBackgroundColor: Change the floating button background color.
* fabIconSrc: Give a icon will be shown on floating button.
```xml
<resources>
    <declare-styleable name="fab">
        <attr name="labelText" format="string" />
        <attr name="labelBackgroundColor" format="integer" />
        <attr name="fabType" format="enum">
            <enum name="normal" value="0"/>
            <enum name="mini" value="1"/>
        </attr>
        <attr name="fabBackgroundColor" format="color" />
        <attr name="fabIconSrc" format="integer" />
    </declare-styleable>
</resources>
```

## Usage

As most other customed layout class, you can simply declare the class FloatingButton in your code.
In FabMenuActivitiy.java, it uses a FloatingButtonGroup class to control all floating buttons, but it just
a sample and you can customize yours. 

