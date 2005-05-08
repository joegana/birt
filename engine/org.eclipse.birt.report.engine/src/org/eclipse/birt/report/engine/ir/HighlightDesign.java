/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;

/**
 * 
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:08:26 $
 */
public class HighlightDesign
{

	/**
	 * test expression
	 */
	protected String testExpression;

	/**
	 * rules in this highlight
	 */
	protected ArrayList rules = new ArrayList( );

	/**
	 * @return Returns the testExpression.
	 */
	public String getTestExpression( )
	{
		return testExpression;
	}

	/**
	 * @param testExpression
	 *            The testExpression to set.
	 */
	public void setTestExpression( String testExpression )
	{
		this.testExpression = testExpression;
	}

	/**
	 * get rule count.
	 * 
	 * @return rule count
	 */
	public int getRuleCount( )
	{
		return this.rules.size( );
	}

	/**
	 * add rule into this hightlight.
	 * 
	 * @param rule
	 *            rule to be added
	 */
	public void addRule( HighlightRuleDesign rule )
	{
		this.rules.add( rule );
	}

	/**
	 * get rule at index.
	 * 
	 * @param index
	 *            index of the rule
	 * @return rule defined in this hightlight
	 */
	public HighlightRuleDesign getRule( int index )
	{
		assert ( index >= 0 && index < this.rules.size( ) );
		return (HighlightRuleDesign) this.rules.get( index );
	}
}
