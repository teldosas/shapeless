/*
 * Copyright (c) 2014 Miles Sabin 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shapeless

import scala.language.experimental.macros

import scala.reflect.macros.Context

package object test {
  def typed[T](t : => T) {}

  def sameTyped[T](t1: => T)(t2: => T) {}

  def showType[T]: String = macro TestMacros.showTypeNoValue[T]

  def showType[T](t: => T): String = macro TestMacros.showType[T]
}

class TestMacros[C <: Context](val c: C) {
  import c.universe._

  def showTypeNoValue[T: WeakTypeTag]: Tree = q"${weakTypeOf[T].normalize.toString}"

  def showType[T: WeakTypeTag](t: Tree): Tree = showTypeNoValue[T]
}

object TestMacros {
  def inst(c: Context) = new TestMacros[c.type](c)

  def showTypeNoValue[T: c.WeakTypeTag](c: Context): c.Expr[String] =
    c.Expr[String](inst(c).showTypeNoValue[T])

  def showType[T: c.WeakTypeTag](c: Context)(t: c.Expr[T]): c.Expr[String] =
    c.Expr[String](inst(c).showType[T](t.tree))
}
