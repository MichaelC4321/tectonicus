/*
 * Copyright (c) 2012-2017, John Campbell and other contributors.  All rights reserved.
 *
 * This file is part of Tectonicus. It is subject to the license terms in the LICENSE file found in
 * the top-level directory of this distribution.  The full list of project contributors is contained
 * in the AUTHORS file found in the same location.
 *
 */

package tectonicus.rasteriser;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import tectonicus.blockTypes.BlockUtil;
import tectonicus.BlockContext;
import tectonicus.BlockType;
import tectonicus.blockTypes.BlockModel.BlockElement;
import tectonicus.blockTypes.BlockModel.BlockElement.ElementFace;
import tectonicus.configuration.LightFace;
import tectonicus.raw.RawChunk;
import tectonicus.renderer.Geometry;
import tectonicus.renderer.Geometry.MeshType;
import tectonicus.texture.SubTexture;
import tectonicus.util.Colour4f;

public class MeshUtil
{

	public static void addCube(final float x, final float y, final float z, Vector4f colour, final boolean addTop,
								final boolean addNorth, final boolean addSouth, final boolean addEast, final boolean addWest,
								Mesh geometry)
	{
		final float inc = 0.2f;
		Vector4f lightColour = new Vector4f(colour.x + inc, colour.y + inc, colour.z + inc, colour.w);
		Vector4f darkColour = new Vector4f(colour.x - inc, colour.y - inc, colour.z - inc, colour.w);
		
		BlockUtil.clamp(lightColour);
		BlockUtil.clamp(darkColour);
		
		// Top
		if (addTop)
		{
			geometry.addVertex(new Vector3f(x, y+1, z), colour, 0, 0);
			geometry.addVertex(new Vector3f(x+1, y+1, z), colour, 1, 0);
			geometry.addVertex(new Vector3f(x+1, y+1, z+1), colour, 1, 1);
			geometry.addVertex(new Vector3f(x, y+1, z+1), colour, 0, 1);
		}
		
		// North face
		if (addNorth)
		{
			geometry.addVertex(new Vector3f(x, y, z), darkColour, 0, 0);
			geometry.addVertex(new Vector3f(x, y+1, z), darkColour, 1, 0);
			geometry.addVertex(new Vector3f(x, y+1, z+1), darkColour, 1, 1);
			geometry.addVertex(new Vector3f(x, y, z+1), darkColour, 0, 1);	
		}
		
		// South
		if (addSouth)
		{
			geometry.addVertex(new Vector3f(x+1, y, z), darkColour, 0, 0);
			geometry.addVertex(new Vector3f(x+1, y, z+1), darkColour, 0, 1);
			geometry.addVertex(new Vector3f(x+1, y+1, z+1), darkColour, 1, 1);
			geometry.addVertex(new Vector3f(x+1, y+1, z), darkColour, 1, 0);
		}
		
		// East
		if (addEast)
		{
			geometry.addVertex(new Vector3f(x+1, y+1, z), lightColour, 1, 1);
			geometry.addVertex(new Vector3f(x, y+1, z), lightColour, 0, 1);
			geometry.addVertex(new Vector3f(x, y, z), lightColour, 0, 0);
			geometry.addVertex(new Vector3f(x+1, y, z), lightColour, 1, 0);
		}
		
		// West
		if (addWest)
		{
			geometry.addVertex(new Vector3f(x, y, z+1), lightColour, 0, 0);
			geometry.addVertex(new Vector3f(x, y+1, z+1), lightColour, 0, 1);
			geometry.addVertex(new Vector3f(x+1, y+1, z+1), lightColour, 1, 1);
			geometry.addVertex(new Vector3f(x+1, y, z+1), lightColour, 1, 0);
		}
	}

	public static void addQuad(Mesh mesh, Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, Vector4f colour, SubTexture texture)
	{
		mesh.addVertex(p0, colour, texture.u0, texture.v0);
		mesh.addVertex(p1, colour, texture.u1, texture.v0);
		mesh.addVertex(p2, colour, texture.u1, texture.v1);
		mesh.addVertex(p3, colour, texture.u0, texture.v1);
	}
	
	public static void addDoubleSidedQuad(Mesh mesh, Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, Vector4f colour, SubTexture texture)
	{
		// Clockwise
		mesh.addVertex(p0, colour, texture.u0, texture.v0);
		mesh.addVertex(p1, colour, texture.u1, texture.v0);
		mesh.addVertex(p2, colour, texture.u1, texture.v1);
		mesh.addVertex(p3, colour, texture.u0, texture.v1);
		
		// Anticlockwise
		mesh.addVertex(p0, colour, texture.u0, texture.v0);
		mesh.addVertex(p3, colour, texture.u0, texture.v1);
		mesh.addVertex(p2, colour, texture.u1, texture.v1);
		mesh.addVertex(p1, colour, texture.u1, texture.v0);
	}
	
	public static void addDoubleSidedQuad(Mesh mesh, Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, Vector4f colour, Vector2f uv0, Vector2f uv1, Vector2f uv2, Vector2f uv3)
	{
		// Clockwise
		mesh.addVertex(p0, colour, uv0.x, uv0.y);
		mesh.addVertex(p1, colour, uv1.x, uv1.y);
		mesh.addVertex(p2, colour, uv2.x, uv2.y);
		mesh.addVertex(p3, colour, uv3.x, uv3.y);
		
		// Anticlockwise
		mesh.addVertex(p0, colour, uv0.x, uv0.y);
		mesh.addVertex(p3, colour, uv3.x, uv3.y);
		mesh.addVertex(p2, colour, uv2.x, uv2.y);
		mesh.addVertex(p1, colour, uv1.x, uv1.y);
	}

	public static void addQuad(Mesh mesh, Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, Vector4f colour, Vector2f uv0, Vector2f uv1, Vector2f uv2, Vector2f uv3)
	{
		mesh.addVertex(p0, colour, uv0.x, uv0.y);
		mesh.addVertex(p1, colour, uv1.x, uv1.y);
		mesh.addVertex(p2, colour, uv2.x, uv2.y);
		mesh.addVertex(p3, colour, uv3.x, uv3.y);
	}
	
	public static void addBlock(BlockContext world, RawChunk rawChunk, int x, int y, int z, List<BlockElement> elements, Geometry geometry, int rotation, String axis)
	{
		org.joml.Vector3f rotOrigin = new org.joml.Vector3f(x + 0.5f, y + 0.5f, z + 0.5f);
		org.joml.Vector3f rotAxis = new org.joml.Vector3f(0, 1, 0);
		if (axis.equals("x"))
			rotAxis = new org.joml.Vector3f(1, 0, 0);
		
		Matrix4f blockRotation = new Matrix4f().translate(rotOrigin)
				  							   .rotate((float) Math.toRadians(rotation), rotAxis.x, rotAxis.y, 0) 
				  							   .translate(rotOrigin.negate());
		
		BlockType above = world.getBlockType(rawChunk.getChunkCoord(), x, y+1, z);
		BlockType below = world.getBlockType(rawChunk.getChunkCoord(), x, y-1, z);
		BlockType north = world.getBlockType(rawChunk.getChunkCoord(), x, y, z-1);
		BlockType south = world.getBlockType(rawChunk.getChunkCoord(), x, y, z+1);
		BlockType east = world.getBlockType(rawChunk.getChunkCoord(), x+1, y, z);
		BlockType west = world.getBlockType(rawChunk.getChunkCoord(), x-1, y, z);
		
		float topLight = world.getLight(rawChunk.getChunkCoord(), x, y+1, z, LightFace.Top);
		float bottomLight = world.getLight(rawChunk.getChunkCoord(), x, y-1, z, LightFace.Top);
		float northLight = world.getLight(rawChunk.getChunkCoord(), x, y, z-1, LightFace.NorthSouth);
		float southLight = world.getLight(rawChunk.getChunkCoord(), x, y, z+1, LightFace.NorthSouth);
		float eastLight = world.getLight(rawChunk.getChunkCoord(), x+1, y, z, LightFace.EastWest);
		float westLight = world.getLight(rawChunk.getChunkCoord(), x-1, y, z, LightFace.EastWest);
		
		if (axis.equals("x") && Math.abs(rotation) == 90)
		{
			above = world.getBlockType(rawChunk.getChunkCoord(), x, y, z+1);
			below = world.getBlockType(rawChunk.getChunkCoord(), x, y, z-1);
			north = world.getBlockType(rawChunk.getChunkCoord(), x, y+1, z);
			south = world.getBlockType(rawChunk.getChunkCoord(), x, y-1, z);
			
			topLight = world.getLight(rawChunk.getChunkCoord(), x, y, z+1, LightFace.NorthSouth);
			bottomLight = world.getLight(rawChunk.getChunkCoord(), x, y, z-1, LightFace.NorthSouth);
			northLight = world.getLight(rawChunk.getChunkCoord(), x, y+1, z, LightFace.Top);
			southLight = world.getLight(rawChunk.getChunkCoord(), x, y-1, z, LightFace.Top);
		}
		if (axis.equals("x") && Math.abs(rotation) == 270)
		{
			above = world.getBlockType(rawChunk.getChunkCoord(), x, y, z-1);
			below = world.getBlockType(rawChunk.getChunkCoord(), x, y, z+1);
			north = world.getBlockType(rawChunk.getChunkCoord(), x, y-1, z);
			south = world.getBlockType(rawChunk.getChunkCoord(), x, y+1, z);
			
			topLight = world.getLight(rawChunk.getChunkCoord(), x, y, z-1, LightFace.NorthSouth);
			bottomLight = world.getLight(rawChunk.getChunkCoord(), x, y, z+1, LightFace.NorthSouth);
			northLight = world.getLight(rawChunk.getChunkCoord(), x, y-1, z, LightFace.Top);
			southLight = world.getLight(rawChunk.getChunkCoord(), x, y+1, z, LightFace.Top);
		}
		else if (axis.equals("y") && Math.abs(rotation) == 90)
		{
			north = world.getBlockType(rawChunk.getChunkCoord(), x+1, y, z);
			south = world.getBlockType(rawChunk.getChunkCoord(), x-1, y, z);
			east = world.getBlockType(rawChunk.getChunkCoord(), x, y, z+1);
			west = world.getBlockType(rawChunk.getChunkCoord(), x, y, z-1);
			
			northLight = world.getLight(rawChunk.getChunkCoord(), x+1, y, z, LightFace.EastWest);
			southLight = world.getLight(rawChunk.getChunkCoord(), x-1, y, z, LightFace.EastWest);
			eastLight = world.getLight(rawChunk.getChunkCoord(), x, y, z+1, LightFace.NorthSouth);
			westLight = world.getLight(rawChunk.getChunkCoord(), x, y, z-1, LightFace.NorthSouth);
		}
		else if (Math.abs(rotation) == 180)
		{
			north = world.getBlockType(rawChunk.getChunkCoord(), x, y, z+1);
			south = world.getBlockType(rawChunk.getChunkCoord(), x, y, z-1);
			east = world.getBlockType(rawChunk.getChunkCoord(), x-1, y, z);
			west = world.getBlockType(rawChunk.getChunkCoord(), x+1, y, z);
			
			northLight = world.getLight(rawChunk.getChunkCoord(), x, y, z+1, LightFace.NorthSouth);
			southLight = world.getLight(rawChunk.getChunkCoord(), x, y, z-1, LightFace.NorthSouth);
			eastLight = world.getLight(rawChunk.getChunkCoord(), x-1, y, z, LightFace.EastWest);
			westLight = world.getLight(rawChunk.getChunkCoord(), x+1, y, z, LightFace.EastWest);
		}
		if (axis.equals("y") && Math.abs(rotation) == 270)
		{
			north = world.getBlockType(rawChunk.getChunkCoord(), x-1, y, z);
			south = world.getBlockType(rawChunk.getChunkCoord(), x+1, y, z);
			east = world.getBlockType(rawChunk.getChunkCoord(), x, y, z-1);
			west = world.getBlockType(rawChunk.getChunkCoord(), x, y, z+1);
			
			northLight = world.getLight(rawChunk.getChunkCoord(), x-1, y, z, LightFace.EastWest);
			southLight = world.getLight(rawChunk.getChunkCoord(), x+1, y, z, LightFace.EastWest);
			eastLight = world.getLight(rawChunk.getChunkCoord(), x, y, z-1, LightFace.NorthSouth);
			westLight = world.getLight(rawChunk.getChunkCoord(), x, y, z+1, LightFace.NorthSouth);
		}
		
		
		for(BlockElement element : elements)
		{
			org.joml.Vector3f rotationOrigin = element.getRotationOrigin().div(16);
			org.joml.Vector3f rotationAxis = element.getRotationAxis();
			
			Matrix4f elementRotation = null;
			if (element.getRotationAngle() != 0)
			{
				elementRotation = new Matrix4f().translate(rotationOrigin)
									              .rotate((float) Math.toRadians(element.getRotationAngle()), rotationAxis.x, rotationAxis.y, rotationAxis.z)
									              .translate(rotationOrigin.negate());
			}
			
			float x1 = x + element.getFrom().x()/16;
			float y1 = y + element.getFrom().y()/16;
			float z1 = z + element.getFrom().z()/16;
	        
			float x2 = x + element.getTo().x()/16;
			float y2 = y + element.getTo().y()/16;
			float z2 = z + element.getTo().z()/16;
	
	        org.joml.Vector3f topLeft, topRight, bottomRight, bottomLeft;
	        
			if (element.getFaces().containsKey("up") && !above.isSolid())
	        {
				Colour4f color = new Colour4f(1 * topLight, 1 * topLight, 1 * topLight, 1);
				
				ElementFace face = element.getFaces().get("up");
				
				topLeft = new org.joml.Vector3f(x1, y2, z1);
		        topRight = new org.joml.Vector3f(x2, y2, z1);
		        bottomRight = new org.joml.Vector3f(x2, y2, z2);
		        bottomLeft = new org.joml.Vector3f(x1, y2, z2);
		        
		        addVertices(geometry, color, face, topLeft, topRight, bottomRight, bottomLeft, elementRotation, blockRotation);
	        }
			
			if (element.getFaces().containsKey("down") && !below.isSolid())
	        {
				Colour4f color = new Colour4f(1 * bottomLight, 1 * bottomLight, 1 * bottomLight, 1);
				
				ElementFace face = element.getFaces().get("down");
				
				topLeft = new org.joml.Vector3f(x1, y1, z2);
		        topRight = new org.joml.Vector3f(x2, y1, z2);
		        bottomRight = new org.joml.Vector3f(x2, y1, z1);
		        bottomLeft = new org.joml.Vector3f(x1, y1, z1);
		        
		        addVertices(geometry, color, face, topLeft, topRight, bottomRight, bottomLeft, elementRotation, blockRotation);
	        }
			
			if (element.getFaces().containsKey("north") && !north.isSolid())
	        {
				Colour4f color = new Colour4f(1 * northLight, 1 * northLight, 1 * northLight, 1);
				
				ElementFace face = element.getFaces().get("north");
				
				topLeft = new org.joml.Vector3f(x2, y2, z1);
		        topRight = new org.joml.Vector3f(x1, y2, z1);
		        bottomRight = new org.joml.Vector3f(x1, y1, z1);
		        bottomLeft = new org.joml.Vector3f(x2, y1, z1);
		        
		        addVertices(geometry, color, face, topLeft, topRight, bottomRight, bottomLeft, elementRotation, blockRotation);
	        }
			
			if (element.getFaces().containsKey("south") && !south.isSolid())
	        {
				Colour4f color = new Colour4f(1 * southLight, 1 * southLight, 1 * southLight, 1);
				
				ElementFace face = element.getFaces().get("south");
				
				topLeft = new org.joml.Vector3f(x1, y2, z2);
		        topRight = new org.joml.Vector3f(x2, y2, z2);
		        bottomRight = new org.joml.Vector3f(x2, y1, z2);
		        bottomLeft = new org.joml.Vector3f(x1, y1, z2);
		        
		        addVertices(geometry, color, face, topLeft, topRight, bottomRight, bottomLeft, elementRotation, blockRotation);
	        }
			
			if (element.getFaces().containsKey("east") && !east.isSolid())
	        {
				Colour4f color = new Colour4f(1 * eastLight, 1 * eastLight, 1 * eastLight, 1);
				
				ElementFace face = element.getFaces().get("east");
				
				topLeft = new org.joml.Vector3f(x2, y2, z2);
		        topRight = new org.joml.Vector3f(x2, y2, z1);
		        bottomRight = new org.joml.Vector3f(x2, y1, z1);
		        bottomLeft = new org.joml.Vector3f(x2, y1, z2);
		        
		        addVertices(geometry, color, face, topLeft, topRight, bottomRight, bottomLeft, elementRotation, blockRotation);
	        }
			
			if (element.getFaces().containsKey("west") && !west.isSolid())
	        {
				Colour4f color = new Colour4f(1 * westLight, 1 * westLight, 1 * westLight, 1);
				
				ElementFace face = element.getFaces().get("west");
				
				topLeft = new org.joml.Vector3f(x1, y2, z1);
		        topRight = new org.joml.Vector3f(x1, y2, z2);
		        bottomRight = new org.joml.Vector3f(x1, y1, z2);
		        bottomLeft = new org.joml.Vector3f(x1, y1, z1);
		        
		        addVertices(geometry, color, face, topLeft, topRight, bottomRight, bottomLeft, elementRotation, blockRotation);
	        }
		}
	}
	
	private static void addVertices(Geometry geometry, Colour4f color, ElementFace face, org.joml.Vector3f topLeft, org.joml.Vector3f topRight, org.joml.Vector3f bottomRight, org.joml.Vector3f bottomLeft, org.joml.Matrix4f rotationTransform, Matrix4f rotTransform)
	{
		rotTransform.transformPosition(topLeft);
        rotTransform.transformPosition(topRight);
        rotTransform.transformPosition(bottomRight);
        rotTransform.transformPosition(bottomLeft);
		
		if(rotationTransform != null)
        {
	        rotationTransform.transformPosition(topLeft);
	        rotationTransform.transformPosition(topRight);
	        rotationTransform.transformPosition(bottomRight);
	        rotationTransform.transformPosition(bottomLeft);
        }
		
		SubTexture tex = face.getTexture();
		Mesh mesh = geometry.getMesh(tex.texture, MeshType.AlphaTest);
		
		int texRotation = face.getTextureRotation();
		if(texRotation == 0)
		{
			mesh.addVertex(topLeft, color, tex.u0, tex.v0);
			mesh.addVertex(topRight, color, tex.u1, tex.v0);
			mesh.addVertex(bottomRight, color, tex.u1, tex.v1);
			mesh.addVertex(bottomLeft, color, tex.u0, tex.v1);
		}
		else if (texRotation == 90)
		{
			mesh.addVertex(topLeft, color, tex.u0, tex.v1);
			mesh.addVertex(topRight, color, tex.u0, tex.v0);
			mesh.addVertex(bottomRight, color, tex.u1, tex.v0);
			mesh.addVertex(bottomLeft, color, tex.u1, tex.v1);
		}
		else if (texRotation == 180)
		{
			mesh.addVertex(topLeft, color, tex.u1, tex.v1);
			mesh.addVertex(topRight, color, tex.u0, tex.v1);
			mesh.addVertex(bottomRight, color, tex.u0, tex.v0);
			mesh.addVertex(bottomLeft, color, tex.u1, tex.v0);
		}
		else if (texRotation == 270)
		{
			mesh.addVertex(topLeft, color, tex.u1, tex.v0);
			mesh.addVertex(topRight, color, tex.u1, tex.v1);
			mesh.addVertex(bottomRight, color, tex.u0, tex.v1);
			mesh.addVertex(bottomLeft, color, tex.u0, tex.v0);
		}
	}
}
