/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl.index;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

public class SerializableBirtHash extends HashMap implements IIndexSerializer
{

	public static int NULL_VALUE_OFFSET = -2;
	public static int NOT_HASH_VALUE_OFFSET = -3;

	private boolean closed = false;
	private HashSet valueSet = new HashSet( );
	private StreamManager manager;
	private String indexName;
	private String valueName;
	public SerializableBirtHash( String indexName, String valueName, StreamManager manager )
	{
		super( );
		this.indexName = indexName;
		this.valueName = valueName;
		this.manager = manager;
	}

	public Object put( Object key, Object value )
	{
		if ( key == null )
			this.valueSet.add( null );
		else
		{
			int hash = key.hashCode( );
			if ( this.valueSet.contains( hash ) )
			{
				this.valueSet.add( key );
			}
			else
			{
				this.valueSet.add( key.hashCode( ) );
			}
		}
		return super.put( key, value );
	}

	public Object getKeyValue( Object key )
	{
		if ( key == null )
			return null;
		if ( this.valueSet.contains( key ) )
			return key;
		return key.hashCode( );
	}

	public void close( ) throws DataException
	{
		if ( closed )
			return;
		this.closed = true;

		this.doSave( );

	}

	private void doSave( ) throws DataException
	{
		try
		{
			if( this.keySet( ).size( ) == 0 )
				return;
			RAOutputStream indexStream = this.manager.getOutStream( indexName  );
			RAOutputStream valueStream = this.manager.getOutStream( valueName );
			DataOutputStream dis = new DataOutputStream( indexStream );
			DataOutputStream dvs = new DataOutputStream( valueStream );
			IOUtil.writeInt( dis, this.keySet( ).size( ) );
			Iterator keyIt = this.keySet( ).iterator( );
			while ( keyIt.hasNext( ) )
			{
				Object key = keyIt.next( );
				// For null value, we do not write the value to value stream
				if ( key == null )
				{
					IOUtil.writeLong( dis, NULL_VALUE_OFFSET );
					IOUtil.writeList( dis, (List) this.get( key ) );
					continue;
				}
				int hash = key == null ? 0 : key.hashCode( );
				if ( !this.valueSet.contains( key ) )
				{
					IOUtil.writeLong( dis, valueStream.getOffset( ) );
					IOUtil.writeInt( dis, hash );
					IOUtil.writeList( dis, (List) this.get( key ) );
					IOUtil.writeString( dvs, key.toString( ) );
				}
				else
				{
					IOUtil.writeLong( dis, NOT_HASH_VALUE_OFFSET );
					IOUtil.writeString( dis, key.toString( ) );
					IOUtil.writeList( dis, (List) this.get( key ) );
				}
			}
			indexStream.close( );
			valueStream.close( );
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}
}
