# 1 - Chapter 1



## Population and Sample

Subjects: The entities that we measure in a study

Population: Set of all subjects of interest

Sample: Subset of population for which we have data


## Descriptive and Inferential Statistics

Descriptive statistics: Refers to methods for summarizing collected data (e.g. graphs, numbers such as averages...)

Inferential statistics: Methods of making decisions or predictions about a population, based on data from the sample


## Sample Statistics and Parameters

Parameter: Numerical summary of the population.
__We use sample statistics to estimate the parameter values__


## Discrete and Continuous data

Variable: Any characteristic observed in a study

Categorical variable: If each observation belongs to a category (gender (nominal), blood type (nominal), letter grade (ordinal), etc.)
Quantitative variable: If values take on numerical values representing different magnitudes of the variable (e.g. commute time, # of siblings, weight)

Discrete data: If possible values are natural numbers
Continuous data: If possible values form an interval (real numbers)


## Mean and Median

Mean formula:
- Informative, but sensitive to outliers)

Median formula:
- Less informative than mean, but resistant to outliers


## Measures of location: Percentiles, Quantiles, Quartiles:

The pth percentile is a value _p_ such that _p_ percent of the observations fall below or at that value.
The _p_th percentile is also called the p/100 quantile

Quartiles:
The first quartile Q1 is the 0.25 quantile (the 25th percentile)
The second quartile Q2 is the 0.5 quantile  (the 50th percentile)
The third quartiile Q3 is the 0.75 quantile (the 75th percentile)

Q1 is the median of the lower half
Q2 is the median
Q3 is the median of the upper half

R example:
```
x <- c(0.3, 0.4, 0.8, 1.4, 1.8, 2.1, 5.9, 11.6, 16.9)

quantile(x, c(0.25, 0.5, 0.75), type=6) # quartiles
25%     50%     75%
0.69    1.80    8.75
```

## Range, Variance, Standard deviation

Range: Difference between largest and smallest

Variance (s^2): Sum_{i=1}^{n} ( (xi - xbar)^2 / n-1 )
Standard deviation (s): sqrt(s^2)

Interquartile Range (IQR): Q3 - Q1


* Range uses only the smallest and largest observations (sensitive to outliers, not used much)
* Standard deviation uses all observations (sensitive to outliers)
* IQR is not affected by outliers, used over s when there are severe outliers


## Histograms


## Shape of a distribution

* Symmetric
* Skewed to the left (most observations are on the RIGHT)
* Skewed to the right (most observations are on the LEFT)


## Detecting potential outliers

An observation is a potential outlier if it falls more than 1.5x IQR below Q1 or more than 1.5x IQR above Q2


## Five-number Summary

1. The minimum
2. Q1
3. Q2 (Median)
4. Q3
5. Maximum


## Box plots

- Box goes from Q1 to Q3
- Vertical line in the box at the median (NOT necessarily in the middle of the box)
- Outer line from outside of the box to the largest value that is not a potential outlier
- Outer line from the outside of the box to the smallest value that is not a potential outlier
- Potential outliers are shown separately


## Boxplot vs. Histogram

* Boxplot does not portray certain features of a distribution, e.g. mounds, gaps
* Boxplot _does_ indicate skew, and can identify potential outliers


## Response variables & Explanatory variables

Response variable (dependent variable): Outcome variable on which comparisons are made (GPA, survival status...)

Categorical explanatory variable (independent variable): Defines the groups to be compared with respect to values for the response variable (e.g. smoking status)

Quantitative explanatory variable (independent variable): Defines the change in different numerical values to be compared with respect to values for the response variable


## Scatterplot

* Graphical display for 2 quantitative variables using the horizontal (x) axis for the explanatory variable x and the vertical (y) axis for the response variable y.
    - Does _not_ confirm a causal relationship, necessarily
