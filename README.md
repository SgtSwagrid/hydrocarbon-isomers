## The world's best combinatorics problem.

An efficient computational solution for counting structural isomers of pure straight-chain alkanes, written in Java.
Alkane/isomer table: https://en.wikipedia.org/wiki/List_of_straight-chain_alkanes

| Alkane Name | No. Carbons | No. Isomers |
| --- | --- | --- |
| Methane | 1 | 1 |
| Ethane | 2 | 1 |
| Propane | 3 | 1 |
| Butane | 4 | 2 |
| Pentane | 5 | 3 |
| Hexane | 6 | 5 |
| Heptane | 7 | 9 |
| Octane | 8 | 18 |
| Nonane | 9 | 35 |
| Hexane | 10 | 75 |

#### Counting rooted trees.

First, one should note that all isomers of straight-chain alkanes (which are what is being considered here) are trees (cycle-free).  This is because introducing cycles changes the number of hydrogens, thus disqualifying such molecules from isomer status (Note however that introducing double/triple bonds allows the exchange of these bonds for cycles, making this more general case much more difficult).

My approach uses [integer partitions](https://en.wikipedia.org/wiki/Partition_%28number_theory%29) and the [multi-choose](https://en.wikipedia.org/wiki/Multiset) function to recursively enumerate rooted subtrees. That is, the number of rooted trees of size n is defined in terms of the number of ways one could generate some number of smaller subtrees using the remaining n-1 vertices (excluding the root), hence the integer partitions.

#### Counting unrooted trees.

Defines a new notion of 'tree centre', distinct from the traditional definition (so as to avoid any notion of depth), but with similar uniqueness properties. A node is a centre (or bi-centre) if no adjacent subtree exceeds ⌊n/2⌋ nodes. Allows unrooted trees to be counted by only considering rooted trees which are centrally-rooted.

To address any skepticism that my notion of centre is actually both unique and complete, observe that (a) moving along any edge away from a centre would produce, back in the direction travelled from, a tree of size greater than ⌊n/2⌋, and that (b) any tree without a centre could not possibly have a finite number of nodes.

#### Future work.

This could easily be adapted to work with non-pure alkanes (eg. haloalkanes) by paritioning on all combinations of elements (rather than just number of carbons), and specifying the required (no longer maximum) number of bonds for each element (including hydrogen, which could no longer be omitted).

In fact, for the simple case where only a single halogen atom is added, the problem actually becomes much easier, since this can serve as the root of the tree, and so one can simply count rooted trees.

However, in general, where double/triple bonds, or loops (which for our purposes, are equivalent) are considered, the problem becomes much more difficult. One would likely have to resort to generating and storing every possible rooted graph, indexed by some unique key, such that newly generated graphs can quickly, in O(1) time, be compared to all previously generated graphs, so duplicates can be discarded.

#### Limitations.

This does not account for stereoisomers, since I'm not a chemist and don't really know what these are.
This also does not account for molecules which could not exist (despite being a valid graph), due to limited space and them being tightly-wound, coupled with my relucatance to implement physical simulation - I'm but a humble maths guy.

#### Motivation.

This started for no other reason than that it seemed like an interesting challenge. In my Wikipedia browsing, I found that there were 366,319 isomers for icosane, and I wanted to figure out how to calculate this number, challenging some friends to do the same (it was actually more difficult than it sounds). Is this useful? I don't know, probably not. If you're into chemistry and this is evenly remotely related to something useful, I'd love to hear about it.

#### Related work.

Like I said, I also challenged some friends to try this too. Here's a similar solution, written in Python:
https://github.com/TipTaco/AlkaneIsomerSolver

Additionally, the following is a very interesting paper I found on the topic subsequent to making my implementation:
https://prod-ng.sandia.gov/techlib-noauth/access-control.cgi/2004/040960.pdf
