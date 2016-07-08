package com.example.administrator.babygrowth01.babyparadise.toyMall;

/**
 * Created by Administrator on 2016/3/9.
 */

import com.example.administrator.babygrowth01.MyResource;

import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

public class Obj3DView extends RendererActivity {
    private static final String DEBUG_TAG="Gestures";
    private Object3dContainer faceObject3D;
    /** Called when the activity is first created. */    @Override    public void initScene()
    {
        scene.lights().add(new Light());
        scene.lights().add(new Light());

        Light myLight = new Light();
        myLight.position.setZ(150);
        scene.lights().add(myLight);

        IParser myParser = Parser.createParser(Parser.Type.OBJ, getResources(), "com.example.administrator.babygrowth01:raw/camaro_obj", true);
        myParser.parse();

        faceObject3D = myParser.getParsedObject();
        faceObject3D.position().x = faceObject3D.position().y = faceObject3D.position().z = 0;
        faceObject3D.scale().x = faceObject3D.scale().y = faceObject3D.scale().z = 0.799f;
        // Depending on the model you will need to change the scale faceObject3D.scale().x = faceObject3D.scale().y = faceObject3D.scale().z = 0.009f;        scene.addChild(faceObject3D);

        scene.addChild(faceObject3D);

    }
    @Override public void updateScene() {
        if(MyResource.mark){
            faceObject3D.rotation().x+= (MyResource.current_y-MyResource.start_y)/30;
            faceObject3D.rotation().z+= (MyResource.current_x-MyResource.start_x)/30;
        }else {
            faceObject3D.rotation().z+= 0.5;
        }

    }
}