/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler.v3_0.planner.logical.steps

import org.neo4j.cypher.internal.compiler.v3_0.planner.LogicalPlanningTestSupport2
import org.neo4j.cypher.internal.compiler.v3_0.planner.logical.plans.{NodeByLabelScan, NodeHashJoin, Selection}
import org.neo4j.cypher.internal.frontend.v3_0.test_helpers.CypherFunSuite


class SelectHasLabelWithJoinTest extends CypherFunSuite with LogicalPlanningTestSupport2 {
  test("should solve labels with joins") {

    implicit val plan = new given {
      cost = {
        case (_: Selection, _) => 1000.0
        case (_: NodeHashJoin, _) => 20.0
        case (_: NodeByLabelScan, _) => 20.0
      }
    } planFor "MATCH (n:Foo:Bar:Baz) RETURN n"

    plan.plan match {
      case NodeHashJoin(_,
      NodeHashJoin(_,
      NodeByLabelScan(_, _, _),
      NodeByLabelScan(_, _, _)),
      NodeByLabelScan(_, _, _)) => ()
      case _ => fail("Not what we expected!")
    }
  }
}