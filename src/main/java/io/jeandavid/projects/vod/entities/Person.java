/*
 * The MIT License
 *
 * Copyright 2015 jd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.jeandavid.projects.vod.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import org.hibernate.Criteria;
import org.hibernate.annotations.Formula;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author jd
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="person_type", discriminatorType=DiscriminatorType.STRING )
public abstract class Person extends Searchable implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  
  private String firstName;
  private String lastName;

  @ManyToMany
  @JsonIgnore
  private Set<Dvd> dvds = new HashSet<Dvd>();

  public Set<Dvd> getDvds() {
    return dvds;
  }

  @JsonIgnore
  @Formula(value = " concat(firstName, ' ', lastName) ")
  private String fullName;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
  
  public void addDvd(Dvd dvd) {
    if(!getDvds().contains(dvd)) {
      getDvds().add(dvd);
    }
    if(!dvd.getPersons().contains(this)) {
      dvd.getPersons().add(this);
    }
  }
  
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object object);

  @Override
  public String toString() {
    return "io.jeandavid.projects.vod.entities.Person[ id=" + id + " ]";
  }

  public static Criteria search(Criteria criteria, Map<String, Object> fields) {
    if(fields.get("fullName") != null) {
      criteria.add(Restrictions.ilike("fullName", fields.get("fullName").toString(), MatchMode.ANYWHERE));
    } else {
      if(fields.get("firstName") != null) {
        criteria.add(Restrictions.ilike("firstName", fields.get("firstName").toString(), MatchMode.ANYWHERE));
      }
      if(fields.get("lastName") != null) {
        criteria.add(Restrictions.ilike("lastName", fields.get("lastName").toString(), MatchMode.ANYWHERE));
      }      
    }
    return criteria;
  }
  
}