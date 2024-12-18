/* A Bison parser, made by GNU Bison 2.3.  */

/* Skeleton interface for Bison's Yacc-like parsers in C

   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     IDENT = 258,
     FCONST = 259,
     SCONST = 260,
     BCONST = 261,
     XCONST = 262,
     Op = 263,
     ICONST = 264,
     PARAM = 265,
     TYPECAST = 266,
     DOT_DOT = 267,
     COLON_EQUALS = 268,
     EQUALS_GREATER = 269,
     LESS_EQUALS = 270,
     GREATER_EQUALS = 271,
     NOT_EQUALS = 272,
     T_WORD = 273,
     T_CWORD = 274,
     T_DATUM = 275,
     LESS_LESS = 276,
     GREATER_GREATER = 277,
     K_ABSOLUTE = 278,
     K_ALIAS = 279,
     K_ALL = 280,
     K_ARRAY = 281,
     K_ASSERT = 282,
     K_BACKWARD = 283,
     K_BEGIN = 284,
     K_BY = 285,
     K_CASE = 286,
     K_CLOSE = 287,
     K_COLLATE = 288,
     K_COLUMN = 289,
     K_COLUMN_NAME = 290,
     K_CONSTANT = 291,
     K_CONSTRAINT = 292,
     K_CONSTRAINT_NAME = 293,
     K_CONTINUE = 294,
     K_CURRENT = 295,
     K_CURSOR = 296,
     K_DATATYPE = 297,
     K_DEBUG = 298,
     K_DECLARE = 299,
     K_DEFAULT = 300,
     K_DETAIL = 301,
     K_DIAGNOSTICS = 302,
     K_DUMP = 303,
     K_ELSE = 304,
     K_ELSIF = 305,
     K_END = 306,
     K_ERRCODE = 307,
     K_ERROR = 308,
     K_EXCEPTION = 309,
     K_EXECUTE = 310,
     K_EXIT = 311,
     K_FETCH = 312,
     K_FIRST = 313,
     K_FOR = 314,
     K_FOREACH = 315,
     K_FORWARD = 316,
     K_FROM = 317,
     K_GET = 318,
     K_HINT = 319,
     K_IF = 320,
     K_IMPORT = 321,
     K_IN = 322,
     K_INFO = 323,
     K_INSERT = 324,
     K_INTO = 325,
     K_IS = 326,
     K_LAST = 327,
     K_LOG = 328,
     K_LOOP = 329,
     K_MESSAGE = 330,
     K_MESSAGE_TEXT = 331,
     K_MOVE = 332,
     K_NEXT = 333,
     K_NO = 334,
     K_NOT = 335,
     K_NOTICE = 336,
     K_NULL = 337,
     K_OPEN = 338,
     K_OPTION = 339,
     K_OR = 340,
     K_PERFORM = 341,
     K_PG_CONTEXT = 342,
     K_PG_DATATYPE_NAME = 343,
     K_PG_EXCEPTION_CONTEXT = 344,
     K_PG_EXCEPTION_DETAIL = 345,
     K_PG_EXCEPTION_HINT = 346,
     K_PRINT_STRICT_PARAMS = 347,
     K_PRIOR = 348,
     K_QUERY = 349,
     K_RAISE = 350,
     K_RELATIVE = 351,
     K_RESULT_OID = 352,
     K_RETURN = 353,
     K_RETURNED_SQLSTATE = 354,
     K_REVERSE = 355,
     K_ROW_COUNT = 356,
     K_ROWTYPE = 357,
     K_SCHEMA = 358,
     K_SCHEMA_NAME = 359,
     K_SCROLL = 360,
     K_SLICE = 361,
     K_SQLSTATE = 362,
     K_STACKED = 363,
     K_STRICT = 364,
     K_TABLE = 365,
     K_TABLE_NAME = 366,
     K_THEN = 367,
     K_TO = 368,
     K_TYPE = 369,
     K_USE_COLUMN = 370,
     K_USE_VARIABLE = 371,
     K_USING = 372,
     K_VARIABLE_CONFLICT = 373,
     K_WARNING = 374,
     K_WHEN = 375,
     K_WHILE = 376
   };
#endif
/* Tokens.  */
#define IDENT 258
#define FCONST 259
#define SCONST 260
#define BCONST 261
#define XCONST 262
#define Op 263
#define ICONST 264
#define PARAM 265
#define TYPECAST 266
#define DOT_DOT 267
#define COLON_EQUALS 268
#define EQUALS_GREATER 269
#define LESS_EQUALS 270
#define GREATER_EQUALS 271
#define NOT_EQUALS 272
#define T_WORD 273
#define T_CWORD 274
#define T_DATUM 275
#define LESS_LESS 276
#define GREATER_GREATER 277
#define K_ABSOLUTE 278
#define K_ALIAS 279
#define K_ALL 280
#define K_ARRAY 281
#define K_ASSERT 282
#define K_BACKWARD 283
#define K_BEGIN 284
#define K_BY 285
#define K_CASE 286
#define K_CLOSE 287
#define K_COLLATE 288
#define K_COLUMN 289
#define K_COLUMN_NAME 290
#define K_CONSTANT 291
#define K_CONSTRAINT 292
#define K_CONSTRAINT_NAME 293
#define K_CONTINUE 294
#define K_CURRENT 295
#define K_CURSOR 296
#define K_DATATYPE 297
#define K_DEBUG 298
#define K_DECLARE 299
#define K_DEFAULT 300
#define K_DETAIL 301
#define K_DIAGNOSTICS 302
#define K_DUMP 303
#define K_ELSE 304
#define K_ELSIF 305
#define K_END 306
#define K_ERRCODE 307
#define K_ERROR 308
#define K_EXCEPTION 309
#define K_EXECUTE 310
#define K_EXIT 311
#define K_FETCH 312
#define K_FIRST 313
#define K_FOR 314
#define K_FOREACH 315
#define K_FORWARD 316
#define K_FROM 317
#define K_GET 318
#define K_HINT 319
#define K_IF 320
#define K_IMPORT 321
#define K_IN 322
#define K_INFO 323
#define K_INSERT 324
#define K_INTO 325
#define K_IS 326
#define K_LAST 327
#define K_LOG 328
#define K_LOOP 329
#define K_MESSAGE 330
#define K_MESSAGE_TEXT 331
#define K_MOVE 332
#define K_NEXT 333
#define K_NO 334
#define K_NOT 335
#define K_NOTICE 336
#define K_NULL 337
#define K_OPEN 338
#define K_OPTION 339
#define K_OR 340
#define K_PERFORM 341
#define K_PG_CONTEXT 342
#define K_PG_DATATYPE_NAME 343
#define K_PG_EXCEPTION_CONTEXT 344
#define K_PG_EXCEPTION_DETAIL 345
#define K_PG_EXCEPTION_HINT 346
#define K_PRINT_STRICT_PARAMS 347
#define K_PRIOR 348
#define K_QUERY 349
#define K_RAISE 350
#define K_RELATIVE 351
#define K_RESULT_OID 352
#define K_RETURN 353
#define K_RETURNED_SQLSTATE 354
#define K_REVERSE 355
#define K_ROW_COUNT 356
#define K_ROWTYPE 357
#define K_SCHEMA 358
#define K_SCHEMA_NAME 359
#define K_SCROLL 360
#define K_SLICE 361
#define K_SQLSTATE 362
#define K_STACKED 363
#define K_STRICT 364
#define K_TABLE 365
#define K_TABLE_NAME 366
#define K_THEN 367
#define K_TO 368
#define K_TYPE 369
#define K_USE_COLUMN 370
#define K_USE_VARIABLE 371
#define K_USING 372
#define K_VARIABLE_CONFLICT 373
#define K_WARNING 374
#define K_WHEN 375
#define K_WHILE 376




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
#line 117 "pl_gram.y"
{
		core_YYSTYPE			core_yystype;
		/* these fields must match core_YYSTYPE: */
		int						ival;
		char					*str;
		const char				*keyword;

		PLword					word;
		PLcword					cword;
		PLwdatum				wdatum;
		bool					boolean;
		Oid						oid;
		struct
		{
			char *name;
			int  lineno;
		}						varname;
		struct
		{
			char *name;
			int  lineno;
			PLpgSQL_datum   *scalar;
			PLpgSQL_rec		*rec;
			PLpgSQL_row		*row;
		}						forvariable;
		struct
		{
			char *label;
			int  n_initvars;
			int  *initvarnos;
		}						declhdr;
		struct
		{
			List *stmts;
			char *end_label;
			int   end_label_location;
		}						loop_body;
		List					*list;
		PLpgSQL_type			*dtype;
		PLpgSQL_datum			*datum;
		PLpgSQL_var				*var;
		PLpgSQL_expr			*expr;
		PLpgSQL_stmt			*stmt;
		PLpgSQL_condition		*condition;
		PLpgSQL_exception		*exception;
		PLpgSQL_exception_block	*exception_block;
		PLpgSQL_nsitem			*nsitem;
		PLpgSQL_diag_item		*diagitem;
		PLpgSQL_stmt_fetch		*fetch;
		PLpgSQL_case_when		*casewhen;
}
/* Line 1529 of yacc.c.  */
#line 343 "pl_gram.h"
	YYSTYPE;
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif

extern YYSTYPE plpgsql_yylval;

#if ! defined YYLTYPE && ! defined YYLTYPE_IS_DECLARED
typedef struct YYLTYPE
{
  int first_line;
  int first_column;
  int last_line;
  int last_column;
} YYLTYPE;
# define yyltype YYLTYPE /* obsolescent; will be withdrawn */
# define YYLTYPE_IS_DECLARED 1
# define YYLTYPE_IS_TRIVIAL 1
#endif

extern YYLTYPE plpgsql_yylloc;
