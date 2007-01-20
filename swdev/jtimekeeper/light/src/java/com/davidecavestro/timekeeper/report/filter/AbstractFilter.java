/*
 * AbstractFilter.java
 *
 * Created on 30 gennaio 2005, 10.43
 */

package com.davidecavestro.timekeeper.report.filter;

/**
 * Implementazione parziale di filtro.
 *
 * @todo questo design introduce dipendenze cicliche!
 * @author  davide
 */
public abstract class AbstractFilter implements Filter{
	
	/** Costruttore. */
	public AbstractFilter () {
	}
	
    public Filter negate() {
        return new NegateFilter(this);
    }

    public Filter or(Filter filter) {
        return new OrFilter(this, filter);
    }

    public Filter and(Filter filter) {
        return new AndFilter(this, filter);
    }
}
